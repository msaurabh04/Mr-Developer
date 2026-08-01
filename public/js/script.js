// copied from existing script.js
document.addEventListener('DOMContentLoaded', function () {
  // Auto-dismiss alert banners after a few seconds for a cleaner UI
  var alerts = document.querySelectorAll('.alert');
  alerts.forEach(function (alert) {
    setTimeout(function () {
      alert.style.transition = 'opacity 0.5s ease';
      alert.style.opacity = '0';
      setTimeout(function () {
        alert.style.display = 'none';
      }, 500);
    }, 4000);
  });
});
