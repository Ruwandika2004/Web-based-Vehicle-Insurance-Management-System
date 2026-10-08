/**
 * SafeRide Insurance - Authentication & Modal Manager
 */

document.addEventListener('DOMContentLoaded', () => {
  const loginModal = document.getElementById('loginModal');
  const registerModal = document.getElementById('registerModal');
  
  const openLoginBtns = document.querySelectorAll('.btn-open-login');
  const openRegisterBtns = document.querySelectorAll('.btn-open-register');
  const closeBtns = document.querySelectorAll('.modal-close, .btn-modal-close');

  openLoginBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      if (registerModal) registerModal.classList.remove('show');
      if (loginModal) loginModal.classList.add('show');
    });
  });

  openRegisterBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      if (loginModal) loginModal.classList.remove('show');
      if (registerModal) registerModal.classList.add('show');
    });
  });

  closeBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      if (loginModal) loginModal.classList.remove('show');
      if (registerModal) registerModal.classList.remove('show');
    });
  });

  // Login Form Submission
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const username = document.getElementById('loginUsername').value.trim();
      const password = document.getElementById('loginPassword').value;

      try {
        const res = await API.login(username, password);
        API.toast('Welcome back, ' + res.data.fullName, 'success');
        
        setTimeout(() => {
          redirectByRole(res.data.role);
        }, 800);
      } catch (err) {
        // error toasted in api.js
      }
    });
  }

  // Register Form Submission
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const registerData = {
        fullName: document.getElementById('regFullName').value.trim(),
        username: document.getElementById('regUsername').value.trim(),
        email: document.getElementById('regEmail').value.trim(),
        phone: document.getElementById('regPhone').value.trim(),
        password: document.getElementById('regPassword').value,
        role: document.getElementById('regRole') ? document.getElementById('regRole').value : 'ROLE_CUSTOMER',
        nicPassport: document.getElementById('regNic') ? document.getElementById('regNic').value.trim() : null,
        drivingLicenseNo: document.getElementById('regLicense') ? document.getElementById('regLicense').value.trim() : null,
        residentialAddress: document.getElementById('regAddress') ? document.getElementById('regAddress').value.trim() : null,
        city: document.getElementById('regCity') ? document.getElementById('regCity').value.trim() : null
      };

      try {
        const res = await API.register(registerData);
        API.toast('Account registered successfully! Redirecting...', 'success');
        setTimeout(() => {
          redirectByRole(res.data.role);
        }, 1000);
      } catch (err) {
        // handled
      }
    });
  }
});

function redirectByRole(role) {
  switch (role) {
    case 'ROLE_ADMIN':
      window.location.href = '/admin-dashboard.html';
      break;
    case 'ROLE_CLAIMS_OFFICER':
      window.location.href = '/claim-manage.html';
      break;
    case 'ROLE_INSURANCE_AGENT':
      window.location.href = '/staff-dashboard.html';
      break;
    case 'ROLE_CUSTOMER':
    default:
      window.location.href = '/customer-dashboard.html';
      break;
  }
}

window.quickLogin = async function(username, password) {
  try {
    const res = await API.login(username, password);
    API.toast('Logged in as ' + res.data.fullName, 'success');
    setTimeout(() => {
      redirectByRole(res.data.role);
    }, 600);
  } catch (err) {}
};
