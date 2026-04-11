const storageKeys = {
    accessToken: "auth_ui_access_token",
    refreshToken: "auth_ui_refresh_token",
};

function resolveApiBaseUrl() {
    const { protocol, hostname, port } = window.location;

    if ((hostname === "localhost" || hostname === "127.0.0.1") && port === "8080") {
        return window.location.origin;
    }

    return `${protocol}//localhost:8080`;
}

const apiBaseUrl = resolveApiBaseUrl();

const elements = {
    username: document.getElementById("username"),
    email: document.getElementById("email"),
    password: document.getElementById("password"),
    refreshToken: document.getElementById("refresh-token"),
    tokenStatus: document.getElementById("token-status"),
    logOutput: document.getElementById("log-output"),
    authResultStatus: document.getElementById("auth-result-status"),
    authResultBody: document.getElementById("auth-result-body"),
    refreshResultStatus: document.getElementById("refresh-result-status"),
    refreshResultBody: document.getElementById("refresh-result-body"),
    protectedResult: document.getElementById("protected-result"),
    protectedResultStatus: document.getElementById("protected-result-status"),
    protectedResultBody: document.getElementById("protected-result-body"),
    signupBtn: document.getElementById("signup-btn"),
    loginBtn: document.getElementById("login-btn"),
    logoutBtn: document.getElementById("logout-btn"),
    refreshBtn: document.getElementById("refresh-btn"),
    profileBtn: document.getElementById("profile-btn"),
    adminBtn: document.getElementById("admin-btn"),
    clearLogBtn: document.getElementById("clear-log-btn"),
};

function readSignupPayload() {
    return {
        username: elements.username.value.trim(),
        email: elements.email.value.trim(),
        password: elements.password.value,
    };
}

function readLoginPayload() {
    return {
        username: elements.username.value.trim(),
        password: elements.password.value,
    };
}

function syncTokensFromStorage() {
    elements.refreshToken.value = localStorage.getItem(storageKeys.refreshToken) || "";
    updateTokenStatus();
}

function persistTokens(accessToken, refreshToken) {
    if (typeof accessToken === "string") {
        localStorage.setItem(storageKeys.accessToken, accessToken);
    }

    if (typeof refreshToken === "string") {
        localStorage.setItem(storageKeys.refreshToken, refreshToken);
        elements.refreshToken.value = refreshToken;
    }

    updateTokenStatus();
}

function clearTokens() {
    localStorage.removeItem(storageKeys.accessToken);
    localStorage.removeItem(storageKeys.refreshToken);
    elements.refreshToken.value = "";
    updateTokenStatus();
    appendLog("Tokens cleared.");
}

function getStoredAccessToken() {
    return localStorage.getItem(storageKeys.accessToken) || "";
}

function updateTokenStatus() {
    elements.tokenStatus.textContent = getStoredAccessToken()
        ? "Access token is loaded."
        : "Access token is not loaded.";
}

function appendLog(message) {
    const timestamp = new Date().toLocaleTimeString();
    const entry = `[${timestamp}] ${message}`;
    elements.logOutput.textContent = elements.logOutput.textContent
        ? `${entry}\n\n${elements.logOutput.textContent}`
        : entry;
}

function setProtectedResult(statusText, body, isError = false) {
    elements.protectedResultStatus.textContent = statusText;
    elements.protectedResultStatus.classList.toggle("error", isError);
    elements.protectedResultBody.textContent = formatPayload(body);
}

function setAuthResult(statusText, body, isError = false) {
    elements.authResultStatus.textContent = statusText;
    elements.authResultStatus.classList.toggle("error", isError);
    elements.authResultBody.textContent = formatPayload(body);
}

function setRefreshResult(statusText, body, isError = false) {
    elements.refreshResultStatus.textContent = statusText;
    elements.refreshResultStatus.classList.toggle("error", isError);
    elements.refreshResultBody.textContent = formatPayload(body);
}

function formatPayload(payload) {
    if (payload === undefined || payload === null || payload === "") {
        return "(empty)";
    }

    if (typeof payload === "string") {
        return payload;
    }

    return JSON.stringify(payload, null, 2);
}

async function parseResponse(response) {
    const contentType = response.headers.get("content-type") || "";

    if (contentType.includes("application/json")) {
        return response.json();
    }

    return response.text();
}

async function callApi(path, options = {}) {
    const request = {
        method: options.method || "GET",
        headers: {
            ...(options.body ? { "Content-Type": "application/json" } : {}),
            ...(options.withAuth && getStoredAccessToken()
                ? { Authorization: `Bearer ${getStoredAccessToken()}` }
                : {}),
        },
        body: options.body ? JSON.stringify(options.body) : undefined,
    };

    appendLog(`Request ${request.method} ${apiBaseUrl}${path}\n${formatPayload(options.body)}`);

    try {
        const response = await fetch(`${apiBaseUrl}${path}`, request);
        const payload = await parseResponse(response);

        appendLog(`Response ${response.status} ${response.statusText}\n${formatPayload(payload)}`);

        if (!response.ok) {
            const error = new Error(typeof payload === "string" ? payload : response.statusText);
            error.status = response.status;
            error.payload = payload;
            throw error;
        }

        return payload;
    } catch (error) {
        appendLog(`Failed ${request.method} ${path}\n${error.message}`);
        throw error;
    }
}

async function signup() {
    try {
        const credentials = readSignupPayload();
        const payload = await callApi("/auth/v1/signup", {
            method: "POST",
            body: credentials,
        });

        persistTokens(payload.access_token || payload.accessToken, payload.token);
        setAuthResult("Signup response", payload);
    } catch (error) {
        setAuthResult("Signup failed", error.payload || error.message, true);
        throw error;
    }
}

async function login() {
    try {
        const credentials = readLoginPayload();
        const payload = await callApi("/auth/v1/login", {
            method: "POST",
            body: credentials,
        });

        persistTokens(payload.access_token || payload.accessToken, payload.token);
        setAuthResult("Login response", payload);
    } catch (error) {
        const unauthorized = error.status === 401 || error.status === 403;
        setAuthResult(
            unauthorized ? "🔴 Unauthorized for /auth/v1/login" : "Login failed",
            error.payload || error.message,
            true
        );
        throw error;
    }
}

async function refreshAccessToken() {
    const token = elements.refreshToken.value.trim();

    if (!token) {
        appendLog("Refresh token is empty.");
        setRefreshResult("Refresh token is empty", "(empty)", true);
        return;
    }

    try {
        const payload = await callApi("/auth/v1/refreshAccessToken", {
            method: "POST",
            body: { token },
        });

        persistTokens(payload.access_token || payload.accessToken, payload.token);
        setRefreshResult("Refresh response", payload);
    } catch (error) {
        const unauthorized = error.status === 401 || error.status === 403;
        setRefreshResult(
            unauthorized ? "🔴 Unauthorized for /auth/v1/refreshAccessToken" : "Refresh failed",
            error.payload || error.message,
            true
        );
        throw error;
    }
}

async function getUserProfile() {
    try {
        const payload = await callApi("/user/profile", {
            withAuth: true,
        });
        setProtectedResult("User profile response", payload);
    } catch (error) {
        const unauthorized = error.status === 401 || error.status === 403;
        setProtectedResult(
            unauthorized ? "🔴 Unauthorized for /user/profile" : "Request failed for /user/profile",
            error.payload || error.message,
            true
        );
        throw error;
    }
}

async function getAdminDashboard() {
    try {
        const payload = await callApi("/admin/dashboard", {
            withAuth: true,
        });
        setProtectedResult("Admin dashboard response", payload);
    } catch (error) {
        const unauthorized = error.status === 401 || error.status === 403;
        setProtectedResult(
            unauthorized ? "🔴 Unauthorized for /admin/dashboard" : "Request failed for /admin/dashboard",
            error.payload || error.message,
            true
        );
        throw error;
    }
}

elements.signupBtn.addEventListener("click", () => signup().catch(() => {}));
elements.loginBtn.addEventListener("click", () => login().catch(() => {}));
elements.logoutBtn.addEventListener("click", clearTokens);
elements.refreshBtn.addEventListener("click", () => refreshAccessToken().catch(() => {}));
elements.profileBtn.addEventListener("click", () => getUserProfile().catch(() => {}));
elements.adminBtn.addEventListener("click", () => getAdminDashboard().catch(() => {}));
elements.clearLogBtn.addEventListener("click", () => {
    elements.logOutput.textContent = "";
});

syncTokensFromStorage();
appendLog("UI ready.");
