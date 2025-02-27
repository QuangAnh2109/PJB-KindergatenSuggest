setTimeout(function () {
    $(".alert").remove();
}, 3000); // Ẩn thông báo sau 3 giây

$("body").on("click", "button#save-user", function(event) {
    $("#errorFullName").html('');
    $("#errorEmail").html('');
    $("#errorDob").html('');
    $("#errorPhone").html('');
    $("#errorRole").html('');
    $("#errorStatus").html('');

    var count = 0;
    var phoneRegex = /^[0-9]{10,15}$/; // Thay bằng regex từ fa.appcode.common.Constant.phone_regex
    var today = new Date().toISOString().split("T")[0]; // Lấy ngày hôm nay theo định dạng YYYY-MM-DD

    // Kiểm tra Full Name
    if ($("#fullName").val().trim() === '') {
        $("#errorFullName").html('Please enter full name');
        count++;
    }

    // Kiểm tra Date of Birth (DOB)
    var dob = $("#dob").val();
    if (dob.trim() === '') {
        $("#errorDob").html('Please enter date of birth');
        count++;
    } else if (dob >= today) {
        $("#errorDob").html('Date of birth must be in the past');
        count++;
    }

    // Kiểm tra Phone
    var phone = $("#phone").val().trim();
    if (phone === '') {
        $("#errorPhone").html('Please enter phone number');
        count++;
    } else if (!phoneRegex.test(phone)) {
        $("#errorPhone").html('Invalid phone number format');
        count++;
    }

    // Kiểm tra Role
    if ($('#role :selected').val() === '') {
        $("#errorRole").html('Please select a role');
        count++;
    }

    if (count > 0) {
        event.preventDefault(); // Ngăn chặn submit nếu có lỗi
    }
});
