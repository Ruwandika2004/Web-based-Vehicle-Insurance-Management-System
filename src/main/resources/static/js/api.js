/**
 * SafeRide Insurance API Client & Storage Helper
 * Handles unified HTTP requests, local session authentication,
 * dynamic role-based sidebar rendering, and toast notifications.
 */

// Immediate execution to set saved theme before DOM renders (avoids flash of light mode)
(function() {
  try {
    const savedTheme = localStorage.getItem('saferide_theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
  } catch(e) {}
})();

const API_BASE = '/api';

// ─── Role → sidebar definition (all 6 roles) ─────────────────────────────────
const ROLE_SIDEBAR_CONFIG = {

  ROLE_CUSTOMER: {
    badge: { text: 'Customer Account', bg: 'rgba(59, 130, 246, 0.2)', color: '#93c5fd' },
    links: [
      { href: '/customer-dashboard.html', icon: 'fa-home',            label: 'Dashboard Overview',    section: 'Overview' },
      { href: '/vehicle-management.html', icon: 'fa-car',             label: 'Registered Vehicles',   section: 'Coverage & Assets' },
      { href: '/policy-management.html',  icon: 'fa-shield-alt',      label: 'My Insurance Policies', section: 'Coverage & Assets' },
      { href: '/premium-calculator.html', icon: 'fa-calculator',      label: 'Premium Calculator',    section: 'Coverage & Assets' },
      { href: '/claim-submit.html',       icon: 'fa-plus-circle',     label: 'File a Claim',          section: 'Claims' },
      { href: '/claim-track.html',        icon: 'fa-search-location', label: 'Track Claims & Status', section: 'Claims' },
      { href: '/customer-profile.html',   icon: 'fa-user-circle',     label: 'My Profile (KYC)',      section: 'Account' },
    ],
  },

  ROLE_INSURANCE_AGENT: {
    badge: { text: 'Insurance Agent', bg: 'rgba(16, 185, 129, 0.2)', color: '#6ee7b7' },
    links: [
      { href: '/agent-dashboard.html',      icon: 'fa-chart-line', label: 'Agent Dashboard',     section: 'Overview' },
      { href: '/customer-management.html',  icon: 'fa-users',      label: 'Customer Management', section: 'Operations' },
      { href: '/policy-management.html',    icon: 'fa-shield-alt', label: 'Policy Management',   section: 'Operations' },
      { href: '/vehicle-management.html',   icon: 'fa-car',        label: 'Vehicle Registry',    section: 'Operations' },
      { href: '/premium-calculator.html',   icon: 'fa-calculator', label: 'Premium Calculator',  section: 'Operations' },
    ],
  },

  ROLE_CLAIMS_OFFICER: {
    badge: { text: 'Claims Assessor', bg: 'rgba(245, 158, 11, 0.2)', color: '#fcd34d' },
    links: [
      { href: '/claims-dashboard.html',      icon: 'fa-house',          label: 'Claims Dashboard',        section: 'Overview' },
      { href: '/claim-manage.html',          icon: 'fa-gavel',          label: 'Adjudication Queue',      section: 'Operations' },
      { href: '/inspection-management.html', icon: 'fa-clipboard-check',label: 'Technical Damage Reports',section: 'Operations' },
    ],
  },

  ROLE_FINANCE_OFFICER: {
    badge: { text: 'Finance Officer', bg: 'rgba(168, 85, 247, 0.2)', color: '#d8b4fe' },
    links: [
      { href: '/finance-dashboard.html',  icon: 'fa-sack-dollar',         label: 'Finance Dashboard',       section: 'Overview' },
      { href: '/policy-management.html',  icon: 'fa-file-invoice',        label: 'Policy Payment Records',  section: 'Ledger & Payments' },
      { href: '/claim-manage.html',       icon: 'fa-hand-holding-dollar', label: 'Claim Settlement Records',section: 'Ledger & Payments' },
    ],
  },

  ROLE_BRANCH_MANAGER: {
    badge: { text: 'Branch Manager', bg: 'rgba(236, 72, 153, 0.2)', color: '#f472b6' },
    links: [
      { href: '/branch-dashboard.html',      icon: 'fa-chart-bar',      label: 'Branch Dashboard',   section: 'Overview' },
      { href: '/customer-management.html',   icon: 'fa-users',          label: 'Customer Directory', section: 'Operations' },
      { href: '/policy-management.html',     icon: 'fa-shield-alt',     label: 'Sales Overview',     section: 'Operations' },
      { href: '/claim-manage.html',          icon: 'fa-gavel',          label: 'Claims Pipeline',    section: 'Operations' },
      { href: '/vehicle-management.html',    icon: 'fa-car',            label: 'Fleet Registry',     section: 'Operations' },
      { href: '/inspection-management.html', icon: 'fa-clipboard-check',label: 'Inspection Reports', section: 'Operations' },
    ],
  },

  ROLE_ADMIN: {
    badge: { text: 'System Administrator', bg: 'rgba(239, 68, 68, 0.2)', color: '#fca5a5' },
    links: [
      { href: '/admin-dashboard.html',       icon: 'fa-chart-pie',      label: 'Executive Overview', section: 'Overview' },
      { href: '/customer-management.html',   icon: 'fa-users',          label: 'Customer Directory', section: 'Operations' },
      { href: '/claim-manage.html',          icon: 'fa-gavel',          label: 'All Claims',         section: 'Operations' },
      { href: '/inspection-management.html', icon: 'fa-clipboard-check',label: 'Inspections',        section: 'Operations' },
      { href: '/policy-management.html',     icon: 'fa-shield-alt',     label: 'Policies',           section: 'Operations' },
      { href: '/vehicle-management.html',    icon: 'fa-car',            label: 'Vehicles',           section: 'Operations' },
      { href: '/premium-calculator.html',    icon: 'fa-calculator',     label: 'Quote Engine',       section: 'Operations' },
      { href: '/agent-dashboard.html',       icon: 'fa-user-tie',       label: 'Agent Overview',     section: 'Portals' },
      { href: '/claims-dashboard.html',      icon: 'fa-gavel',          label: 'Claims Overview',    section: 'Portals' },
    ],
  },
};

// ─── Page → allowed roles map ─────────────────────────────────────────────────
const PAGE_ALLOWED_ROLES = {
  'customer-dashboard.html':   ['ROLE_CUSTOMER'],
  'customer-profile.html':     ['ROLE_CUSTOMER'],
  'customer-management.html':  ['ROLE_INSURANCE_AGENT', 'ROLE_BRANCH_MANAGER', 'ROLE_ADMIN'],
  'vehicle-management.html':   ['ROLE_CUSTOMER', 'ROLE_INSURANCE_AGENT', 'ROLE_BRANCH_MANAGER', 'ROLE_ADMIN'],
  'policy-management.html':    ['ROLE_CUSTOMER', 'ROLE_INSURANCE_AGENT', 'ROLE_FINANCE_OFFICER', 'ROLE_BRANCH_MANAGER', 'ROLE_ADMIN'],
  'premium-calculator.html':   ['ROLE_CUSTOMER', 'ROLE_INSURANCE_AGENT', 'ROLE_ADMIN'],
  'claim-submit.html':         ['ROLE_CUSTOMER'],
  'claim-track.html':          ['ROLE_CUSTOMER'],
  'claim-manage.html':         ['ROLE_CLAIMS_OFFICER', 'ROLE_FINANCE_OFFICER', 'ROLE_BRANCH_MANAGER', 'ROLE_ADMIN'],
  'inspection-management.html':['ROLE_CLAIMS_OFFICER', 'ROLE_BRANCH_MANAGER', 'ROLE_ADMIN'],
  'agent-dashboard.html':      ['ROLE_INSURANCE_AGENT', 'ROLE_ADMIN'],
  'staff-dashboard.html':      ['ROLE_INSURANCE_AGENT', 'ROLE_ADMIN'],
  'claims-dashboard.html':     ['ROLE_CLAIMS_OFFICER', 'ROLE_ADMIN'],
  'finance-dashboard.html':    ['ROLE_FINANCE_OFFICER', 'ROLE_ADMIN'],
  'branch-dashboard.html':     ['ROLE_BRANCH_MANAGER', 'ROLE_ADMIN'],
  'admin-dashboard.html':      ['ROLE_ADMIN'],
};

const API = {
  // ── Session / Auth ──────────────────────────────────────────────────────────
  getUser() {
    try {
      const user = localStorage.getItem('saferide_user');
      return user ? JSON.parse(user) : null;
    } catch (e) {
      return null;
    }
  },

  setUser(user) {
    localStorage.setItem('saferide_user', JSON.stringify(user));
  },

  logout() {
    localStorage.removeItem('saferide_user');
    window.location.href = '/login.html';
  },

  /**
   * Require authentication. If allowedRoles is empty, any logged-in user is accepted.
   * Redirects to /login.html on failure.
   */
  requireAuth(allowedRoles = []) {
    const user = this.getUser();
    if (!user) {
      window.location.href = '/login.html';
      return null;
    }
    if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
      this.toast('Access Denied — you are not authorised for this section.', 'error');
      setTimeout(() => { window.location.href = '/login.html'; }, 1500);
      return null;
    }
    return user;
  },

  // ── Mobile Drawer State Management ──────────────────────────────────────────
  openSidebarDrawer() {
    document.body.classList.add('sidebar-open');
    const toggleBtn = document.getElementById('sidebarToggleBtn');
    if (toggleBtn) toggleBtn.setAttribute('aria-expanded', 'true');
  },

  closeSidebarDrawer() {
    document.body.classList.remove('sidebar-open');
    const toggleBtn = document.getElementById('sidebarToggleBtn');
    if (toggleBtn) toggleBtn.setAttribute('aria-expanded', 'false');
  },

  toggleSidebarDrawer() {
    if (document.body.classList.contains('sidebar-open')) {
      this.closeSidebarDrawer();
    } else {
      this.openSidebarDrawer();
    }
  },

  initDrawerEvents() {
    // Backdrop initialization
    let backdrop = document.getElementById('sidebarBackdrop');
    if (!backdrop) {
      backdrop = document.createElement('div');
      backdrop.id = 'sidebarBackdrop';
      backdrop.className = 'sidebar-backdrop';
      backdrop.setAttribute('aria-hidden', 'true');
      document.body.appendChild(backdrop);
    }
    backdrop.onclick = () => this.closeSidebarDrawer();

    // Close button in sidebar header if present
    const closeBtn = document.getElementById('sidebarCloseBtn');
    if (closeBtn) {
      closeBtn.onclick = () => this.closeSidebarDrawer();
    }

    // Keyboard accessibility: Escape key closes drawer
    if (!this._drawerKeyBound) {
      window.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && document.body.classList.contains('sidebar-open')) {
          this.closeSidebarDrawer();
        }
      });
      this._drawerKeyBound = true;
    }

    // Auto-close drawer on mobile when clicking navigation links
    const menuEl = document.getElementById('sidebarMenu');
    if (menuEl && !menuEl._clickBound) {
      menuEl.addEventListener('click', (e) => {
        if (e.target.closest('a') && window.innerWidth < 992) {
          this.closeSidebarDrawer();
        }
      });
      menuEl._clickBound = true;
    }
  },

  // ── Top Navigation Bar Initializer ──────────────────────────────────────────
  initNavbar(user) {
    const navContainer = document.querySelector('.navbar .nav-container');
    if (!navContainer) return;

    // 1. Mobile hamburger toggle button
    let toggleBtn = document.getElementById('sidebarToggleBtn');
    if (!toggleBtn) {
      toggleBtn = document.createElement('button');
      toggleBtn.type = 'button';
      toggleBtn.id = 'sidebarToggleBtn';
      toggleBtn.className = 'sidebar-toggle-btn';
      toggleBtn.setAttribute('aria-label', 'Toggle navigation menu');
      toggleBtn.setAttribute('aria-expanded', 'false');
      toggleBtn.innerHTML = '<i class="fas fa-bars"></i>';
      toggleBtn.onclick = (e) => {
        e.stopPropagation();
        this.toggleSidebarDrawer();
      };
      navContainer.insertBefore(toggleBtn, navContainer.firstChild);
    }

    // 2. Desktop Breadcrumb / Page Title
    let breadcrumb = navContainer.querySelector('.nav-breadcrumb');
    if (!breadcrumb) {
      breadcrumb = document.createElement('div');
      breadcrumb.className = 'nav-breadcrumb';
      const page = window.location.pathname.split('/').pop() || '';
      const pageNames = {
        'customer-dashboard.html': 'Customer Overview',
        'customer-profile.html': 'My Profile (KYC)',
        'customer-management.html': 'Customer Directory',
        'vehicle-management.html': 'Vehicle Registry',
        'policy-management.html': 'Policy Management',
        'premium-calculator.html': 'Premium Calculator',
        'claim-submit.html': 'File Insurance Claim',
        'claim-track.html': 'Track Claim Status',
        'claim-manage.html': 'Claims Adjudication Queue',
        'inspection-management.html': 'Technical Damage Reports',
        'agent-dashboard.html': 'Agent Operations Workspace',
        'staff-dashboard.html': 'Staff Operations',
        'claims-dashboard.html': 'Claims Operations Command Centre',
        'finance-dashboard.html': 'Finance & Payments Operations',
        'branch-dashboard.html': 'Branch Operations & Performance',
        'admin-dashboard.html': 'System Administration & Analytics'
      };
      const titleText = pageNames[page] || document.title.split('|')[0].trim() || 'Portal';
      breadcrumb.innerHTML = `
        <span class="breadcrumb-root">SafeRide</span>
        <span class="breadcrumb-sep"><i class="fas fa-chevron-right"></i></span>
        <span class="breadcrumb-current">${titleText}</span>
      `;
      const brandLogo = navContainer.querySelector('.brand-logo');
      if (brandLogo) {
        brandLogo.insertAdjacentElement('afterend', breadcrumb);
      } else {
        navContainer.insertBefore(breadcrumb, navContainer.querySelector('.nav-actions'));
      }
    }

    // 3. Centralized User Account Dropdown in .nav-actions
    const navActions = navContainer.querySelector('.nav-actions');
    if (navActions) {
      // Clean up legacy loose buttons, standalone theme toggles, and logout actions
      navActions.querySelectorAll('.btn-secondary, .btn-nav-logout, button[onclick*="logout"], #themeToggleBtn').forEach(el => el.remove());

      let userDropdown = document.getElementById('navUserDropdown');
      if (!userDropdown) {
        // Clear navActions to guarantee clean mount
        navActions.innerHTML = '';

        const initials = (user.fullName || 'User')
          .split(' ')
          .filter(Boolean)
          .map(n => n[0])
          .slice(0, 2)
          .join('')
          .toUpperCase() || 'U';

        const roleConfig = ROLE_SIDEBAR_CONFIG[user.role];
        const roleLabel = (roleConfig && roleConfig.badge && roleConfig.badge.text)
          ? roleConfig.badge.text
          : (user.role ? user.role.replace('ROLE_', '').replace(/_/g, ' ') : 'Authorized User');

        const isDark = this.getTheme() === 'dark';

        userDropdown = document.createElement('div');
        userDropdown.className = 'nav-user-dropdown';
        userDropdown.id = 'navUserDropdown';
        userDropdown.innerHTML = `
          <button type="button" class="nav-user-trigger" id="navUserTrigger" aria-haspopup="menu" aria-expanded="false" aria-label="User account menu for ${user.fullName || 'User'}">
            <span class="nav-user-avatar" aria-hidden="true">${initials}</span>
            <span id="navUserName" class="nav-user-name">${user.fullName || 'User'}</span>
            <i class="fas fa-chevron-down nav-user-chevron" aria-hidden="true"></i>
          </button>
          <div class="nav-user-menu" id="navUserMenu" role="menu" aria-labelledby="navUserTrigger" hidden>
            <div class="nav-menu-header">
              <div class="nav-menu-name" id="navMenuUserName">${user.fullName || 'User'}</div>
              <div class="nav-menu-role">${roleLabel}</div>
            </div>
            <div class="nav-menu-divider" role="separator"></div>
            <button type="button" class="nav-menu-item theme-menu-item" id="navMenuThemeItem" role="menuitem">
              <i class="fas ${isDark ? 'fa-sun text-amber' : 'fa-moon'}" style="${isDark ? 'color: #fbbf24;' : 'color: #64748b;'}"></i>
              <span>${isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'}</span>
            </button>
            <div class="nav-menu-divider" role="separator"></div>
            <button type="button" class="nav-menu-item logout-menu-item" id="navMenuLogoutItem" role="menuitem">
              <i class="fas fa-sign-out-alt"></i>
              <span>Sign Out</span>
            </button>
          </div>
        `;
        navActions.appendChild(userDropdown);

        const trigger = userDropdown.querySelector('#navUserTrigger');
        const themeBtn = userDropdown.querySelector('#navMenuThemeItem');
        const logoutBtn = userDropdown.querySelector('#navMenuLogoutItem');

        trigger.onclick = (e) => {
          e.stopPropagation();
          this.toggleUserMenu();
        };

        // Keyboard arrow navigation inside dropdown
        userDropdown.addEventListener('keydown', (e) => {
          if (e.key === 'ArrowDown' || e.key === 'ArrowUp') {
            e.preventDefault();
            const items = [themeBtn, logoutBtn];
            const currentIdx = items.indexOf(document.activeElement);
            if (e.key === 'ArrowDown') {
              const next = items[currentIdx + 1] || items[0];
              next.focus();
            } else {
              const prev = items[currentIdx - 1] || items[items.length - 1];
              prev.focus();
            }
          }
        });

        themeBtn.onclick = (e) => {
          e.stopPropagation();
          this.toggleTheme();
        };

        logoutBtn.onclick = (e) => {
          e.stopPropagation();
          this.logout();
        };
      } else {
        const nameEl = document.getElementById('navUserName');
        if (nameEl) nameEl.textContent = user.fullName || 'User';
        const menuNameEl = document.getElementById('navMenuUserName');
        if (menuNameEl) menuNameEl.textContent = user.fullName || 'User';
      }
    }

    this.initUserMenuEvents();
  },

  // ── User Account Dropdown State Management ──────────────────────────────────
  initUserMenuEvents() {
    if (this._userMenuEventsBound) return;
    this._userMenuEventsBound = true;

    // Click outside closes dropdown
    document.addEventListener('click', (e) => {
      const dropdown = document.getElementById('navUserDropdown');
      if (dropdown && !dropdown.contains(e.target)) {
        this.closeUserMenu();
      }
    });

    // Escape closes dropdown and refocuses trigger
    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') {
        const trigger = document.getElementById('navUserTrigger');
        if (trigger && trigger.getAttribute('aria-expanded') === 'true') {
          this.closeUserMenu();
          trigger.focus();
        }
      }
    });
  },

  openUserMenu() {
    const trigger = document.getElementById('navUserTrigger');
    const menu = document.getElementById('navUserMenu');
    if (!trigger || !menu) return;
    trigger.setAttribute('aria-expanded', 'true');
    menu.removeAttribute('hidden');
    menu.classList.add('is-open');
  },

  closeUserMenu() {
    const trigger = document.getElementById('navUserTrigger');
    const menu = document.getElementById('navUserMenu');
    if (!trigger || !menu) return;
    trigger.setAttribute('aria-expanded', 'false');
    menu.setAttribute('hidden', '');
    menu.classList.remove('is-open');
  },

  toggleUserMenu() {
    const trigger = document.getElementById('navUserTrigger');
    if (!trigger) return;
    if (trigger.getAttribute('aria-expanded') === 'true') {
      this.closeUserMenu();
    } else {
      this.openUserMenu();
    }
  },

  // ── Dynamic Sidebar Builder ─────────────────────────────────────────────────
  /**
   * Builds and injects the role-appropriate sidebar.
   * @param {Object} user  - The user object from localStorage.
   * @param {string} [activePage] - Basename of the current page (e.g. 'claim-manage.html').
   *                                Falls back to window.location.pathname if omitted.
   */
  buildSidebar(user, activePage) {
    const sidebar = document.querySelector('aside.sidebar');
    const menuEl = document.getElementById('sidebarMenu');
    if (!menuEl || !sidebar) return;

    // 1. Ensure branding at top of sidebar
    if (!sidebar.querySelector('.sidebar-brand')) {
      const brandDiv = document.createElement('div');
      brandDiv.className = 'sidebar-brand';
      brandDiv.innerHTML = `
        <a href="/" class="sidebar-brand-link" title="SafeRide Home">
          <div class="sidebar-brand-icon">
            <i class="fas fa-shield-alt"></i>
          </div>
          <div class="sidebar-brand-text">SafeRide <span>Insurance</span></div>
        </a>
        <button type="button" class="sidebar-close-btn" id="sidebarCloseBtn" aria-label="Close navigation drawer" onclick="API.closeSidebarDrawer()">
          <i class="fas fa-times"></i>
        </button>
      `;
      sidebar.insertBefore(brandDiv, sidebar.firstChild);
    }

    const page = activePage || window.location.pathname.split('/').pop() || '';
    const config = ROLE_SIDEBAR_CONFIG[user.role];

    if (!config) {
      menuEl.innerHTML = '<li><span style="padding:1rem;color:#94A3B8;">No navigation available.</span></li>';
      return;
    }

    // Inject role badge
    const badgeEl = document.getElementById('sidebarRoleBadge');
    if (badgeEl) {
      badgeEl.textContent = config.badge.text;
      badgeEl.style.background = config.badge.bg;
      badgeEl.style.color = config.badge.color;
    }

    // Build links with categorical grouping
    let currentSection = '';
    let html = '';
    config.links.forEach(link => {
      if (link.section && link.section !== currentSection) {
        currentSection = link.section;
        html += `<li class="sidebar-heading">${currentSection}</li>`;
      }
      const isActive = link.href.endsWith(page) ? 'active' : '';
      html += `<li><a href="${link.href}" class="sidebar-link ${isActive}" ${isActive ? 'aria-current="page"' : ''}>
        <i class="fas ${link.icon}"></i> <span>${link.label}</span>
      </a></li>`;
    });
    menuEl.innerHTML = html;

    // 2. Remove any legacy sidebar footer to avoid duplicate theme/logout controls
    const existingFooter = sidebar.querySelector('.sidebar-footer');
    if (existingFooter) {
      existingFooter.remove();
    }
  },

  /**
   * All-in-one portal initializer: enforces auth, enforces role per page,
   * builds the sidebar, fills user info elements, and returns the user.
   * Call this at the top of every portal page's DOMContentLoaded handler.
   *
   * @param {string[]} [overrideRoles] - If supplied, overrides the PAGE_ALLOWED_ROLES lookup.
   */
  initPortal(overrideRoles) {
    const user = this.getUser();
    if (!user) { window.location.href = '/login.html'; return null; }

    // Determine page
    const page = window.location.pathname.split('/').pop() || '';
    const allowed = overrideRoles || PAGE_ALLOWED_ROLES[page] || [];

    if (allowed.length > 0 && !allowed.includes(user.role)) {
      // Show brief error then redirect to user's proper home
      document.body.innerHTML = `
        <div style="display:flex;align-items:center;justify-content:center;height:100vh;font-family:sans-serif;flex-direction:column;gap:1rem;">
          <div style="font-size:3rem;">🚫</div>
          <h2 style="color:#991b1b;">Access Denied</h2>
          <p style="color:#6b7280;">Your role (<strong>${user.role}</strong>) is not authorised for this page.</p>
          <p style="color:#6b7280;">Redirecting you to your dashboard…</p>
        </div>`;
      setTimeout(() => {
        const home = {
          ROLE_CUSTOMER:        '/customer-dashboard.html',
          ROLE_INSURANCE_AGENT: '/agent-dashboard.html',
          ROLE_CLAIMS_OFFICER:  '/claims-dashboard.html',
          ROLE_FINANCE_OFFICER: '/finance-dashboard.html',
          ROLE_BRANCH_MANAGER:  '/branch-dashboard.html',
          ROLE_ADMIN:           '/admin-dashboard.html',
        }[user.role] || '/login.html';
        window.location.href = home;
      }, 2000);
      return null;
    }

    // Fill shared UI slots (if they exist on this page)
    const setEl = (id, val) => { const el = document.getElementById(id); if (el) el.textContent = val; };
    setEl('navUserName', user.fullName);
    setEl('sidebarUserFullName', user.fullName);
    setEl('sidebarUserEmail', user.email || user.username);

    // Initialize shell architecture
    this.buildSidebar(user);
    this.initNavbar(user);
    this.initDrawerEvents();

    return user;
  },

  // ── Base Fetch wrapper ──────────────────────────────────────────────────────
  async request(endpoint, options = {}) {
    const defaultHeaders = { 'Content-Type': 'application/json' };
    const config = { ...options, headers: { ...defaultHeaders, ...options.headers } };

    try {
      const response = await fetch(`${API_BASE}${endpoint}`, config);
      const data = await response.json();
      if (!response.ok) throw new Error(data.message || 'An error occurred during request execution.');
      return data;
    } catch (error) {
      console.error('API Error:', error);
      this.toast(error.message, 'error');
      throw error;
    }
  },

  // ── Auth endpoints ──────────────────────────────────────────────────────────
  async login(username, password) {
    const res = await this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });
    if (res.success && res.data) this.setUser(res.data);
    return res;
  },

  async register(registerData) {
    const res = await this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(registerData),
    });
    if (res.success && res.data) this.setUser(res.data);
    return res;
  },

  // ── Toast UI ────────────────────────────────────────────────────────────────
  toast(message, type = 'info') {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    const icons = { success: 'fa-check-circle', error: 'fa-exclamation-circle', warning: 'fa-exclamation-triangle', info: 'fa-info-circle' };
    const icon = icons[type] || 'fa-info-circle';

    toast.innerHTML = `<i class="fas ${icon}"></i><div>${message}</div>`;
    container.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateX(100%)';
      toast.style.transition = 'all 0.3s ease';
      setTimeout(() => toast.remove(), 300);
    }, 4000);
  },

  // ── Currency formatter ──────────────────────────────────────────────────────
  formatLKR(amount) {
    if (amount == null) return 'LKR 0.00';
    return 'LKR ' + Number(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  },

  // ── Dark / Light Theme System ────────────────────────────────────────────────
  getTheme() {
    return localStorage.getItem('saferide_theme') || 'light';
  },

  setTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('saferide_theme', theme);
    this.updateThemeButtons(theme);
  },

  toggleTheme() {
    const next = this.getTheme() === 'dark' ? 'light' : 'dark';
    this.setTheme(next);
  },

  updateThemeButtons(theme) {
    const isDark = theme === 'dark';

    // Update topbar user menu theme action button
    const navMenuThemeItem = document.getElementById('navMenuThemeItem');
    if (navMenuThemeItem) {
      const actionText = isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode';
      const iconHtml = isDark
        ? '<i class="fas fa-sun text-amber" style="color: #fbbf24;"></i>'
        : '<i class="fas fa-moon" style="color: #64748b;"></i>';
      navMenuThemeItem.innerHTML = `${iconHtml} <span>${actionText}</span>`;
      navMenuThemeItem.setAttribute('aria-label', actionText);
    }

    const btns = document.querySelectorAll('.theme-toggle-btn');
    btns.forEach(btn => {
      const isCompact = btn.classList.contains('auth-theme-toggle') || btn.classList.contains('icon-only');
      const actionLabel = isDark ? 'Switch to light mode' : 'Switch to dark mode';
      btn.title = actionLabel;
      btn.setAttribute('aria-label', actionLabel);
      if (isCompact) {
        btn.innerHTML = isDark
          ? '<i class="fas fa-sun" style="color: #fbbf24;"></i>'
          : '<i class="fas fa-moon" style="color: #64748b;"></i>';
      } else {
        btn.innerHTML = isDark
          ? '<i class="fas fa-sun" style="color: #fbbf24;"></i> <span>Light</span>'
          : '<i class="fas fa-moon" style="color: #64748b;"></i> <span>Dark</span>';
      }
    });
  },

  injectThemeToggle() {
    // If user account dropdown exists or this is an authenticated portal page,
    // theme toggle is handled exclusively in the topbar account dropdown
    if (document.getElementById('navUserDropdown') || document.querySelector('.navbar .nav-actions')) {
      return;
    }

    if (document.getElementById('themeToggleBtn')) {
      this.updateThemeButtons(this.getTheme());
      return;
    }

    // Do not inject a stray floating button if on dedicated authentication layout
    if (document.querySelector('.auth-page') || document.querySelector('.auth-panel')) {
      return;
    }

    const theme = this.getTheme();
    const btn = document.createElement('button');
    btn.id = 'themeToggleBtn';
    btn.className = 'theme-toggle-btn';
    btn.type = 'button';
    btn.setAttribute('aria-label', 'Toggle light/dark theme');
    btn.onclick = () => this.toggleTheme();
    btn.title = theme === 'dark' ? 'Switch to Light Mode' : 'Switch to Dark Mode';
    btn.innerHTML = theme === 'dark'
      ? '<i class="fas fa-sun" style="color: #fbbf24;"></i> <span>Light</span>'
      : '<i class="fas fa-moon" style="color: #64748b;"></i> <span>Dark</span>';

    // 1. Try top-bar (Login / Landing page)
    const topBar = document.querySelector('.top-bar');
    if (topBar) {
      const rightCol = topBar.querySelector('div:last-child');
      if (rightCol) {
        rightCol.insertBefore(btn, rightCol.firstChild);
        return;
      }
    }

    // 2. Fallback: floating toggle button
    btn.classList.add('theme-toggle-floating');
    document.body.appendChild(btn);
  },
};

// Automatically inject theme toggle across all pages once DOM is ready
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => API.injectThemeToggle());
} else {
  API.injectThemeToggle();
}

