function toggleDropdown(event) {
    event.preventDefault(); // Ngăn chặn hành vi mặc định (nếu có)
    event.stopPropagation(); // Ngăn sự kiện lan truyền

    // Tìm dropdown menu liên quan đến nút được nhấn
    var dropdownMenu = event.currentTarget.nextElementSibling;

    // Đóng tất cả dropdown trước khi mở dropdown mới
    document.querySelectorAll('.dropdown-menu.show').forEach(function(menu) {
        if (menu !== dropdownMenu) {
            menu.classList.remove('show');
        }
    });

    // Toggle dropdown hiện tại
    dropdownMenu.classList.toggle("show");
}

// Sự kiện click trên window để đóng dropdown khi click bên ngoài
window.addEventListener('click', function(event) {
    if (!event.target.closest('.dropdown-toggle') && !event.target.closest('.dropdown-menu')) {
        document.querySelectorAll('.dropdown-menu.show').forEach(function(menu) {
            menu.classList.remove('show');
        });
    }
});
