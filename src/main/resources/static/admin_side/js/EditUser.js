setTimeout(function () {
    $(".alert").remove();
}, 3000); // Ẩn thông báo sau 3 giây

$("body").on("click", "button#save-user", function(event) {
    $(".text-danger").html(""); // Xóa thông báo lỗi cũ

    var count = 0;
    var phoneRegex = /^[0-9]{10,15}$/;
    var today = new Date().toISOString().split("T")[0];
    var emailRegex = /^[a-z][a-z0-9]*@gmail.com/;

    if ($("#fullName").val().trim() === '') {
        $("#errorFullName").html('Please enter full name');
        count++;
    }

    var email = $("#email").val().trim();
    if (email === '') {
        $("#errorEmail").html('Please enter email');
        count++;
    } else if (!emailRegex.test(email)) {
        $("#errorEmail").html('Invalid email format');
        count++;
    }

    var dob = $("#dob").val();
    if (dob.trim() === '') {
        $("#errorDob").html('Please enter date of birth');
        count++;
    } else if (dob >= today) {
        $("#errorDob").html('Date of birth must be in the past');
        count++;
    }

    var phone = $("#phone").val().trim();
    if (phone === '') {
        $("#errorPhone").html('Please enter phone number');
        count++;
    } else if (!phoneRegex.test(phone)) {
        $("#errorPhone").html('Invalid phone number format');
        count++;
    }

    if ($('#role').val() === '') {
        $("#errorRole").html('Please select a role');
        count++;
    }

    if ($('#status').val() === '') {
        $("#errorStatus").html('Please select a status');
        count++;
    }

    if (count > 0) {
        event.preventDefault();
    }
});


// $("body").on("click", "button#save-user", function() {
//     $("#errorFullName").html('');
//     $("#errorEmail").html('');
//     $("#errorDob").html('');
//     $("#errorPhone").html('');
//     $("#errorRole").html('');
//     $("#errorStatus").html('');
//
//     var count = 0;
//     var phoneRegex = /^[0-9]{10,15}$/; // Thay bằng regex từ fa.appcode.common.Constant.phone_regex
//     var today = new Date().toISOString().split("T")[0]; // Lấy ngày hôm nay theo định dạng YYYY-MM-DD
//
//     if ($("#fullName").val().trim() === '') {
//         $("#errorFullName").html('Please enter full name');
//         count++;
//     }
//
//     // Kiểm tra Date of Birth (DOB)
//     var dob = $("#dob").val();
//     if (dob.trim() === '') {
//         $("#errorDob").html('Please enter date of birth');
//         count++;
//     } else if (dob >= today) {
//         $("#errorDob").html('Date of birth must be in the past');
//         count++;
//     }
//     //Kiểm tra Phone
//     var phone = $("#phone").val().trim();
//     if (phone === '') {
//         $("#errorPhone").html('Please enter phone number');
//         count++;
//     } else if (!phoneRegex.test(phone)) {
//         $("#errorPhone").html('Invalid phone number format');
//         count++;
//     }
//     if ($('#role :selected').val() === '') {
//         $("#errorRole").html('Please select a role');
//         count++;
//     }
//
//     if (count === 0) {
//         var user = {
//             id: $("#userID").val(),
//             fullName: $("#fullName").val().trim(),
//             email: $("#email").val().trim(),
//             dob: $("#dob").val().trim(),
//             phone: $("#phone").val().trim(),
//             roleId: $('#role :selected').val(),
//             status: $("#status").val().trim()
//         };
//
//         console.log("User ID: ", $("#userID").val()); // Kiểm tra userID
//         console.log("URL: ", "/admin/api/edit-user/" + $("#userID").val()); // Kiểm tra URL
//         console.log(JSON.stringify(user))
//
//         $.ajax({
//             url: "/admin/api/edit-user",
//             type: "POST",
//             contentType: "application/json",
//             data: JSON.stringify(user),
//             success: function(responseData) {
//                 alert(responseData.message);
//             },
//             error: function(responseData) {
//                 if (responseData.responseJSON) {
//                     $("#errorFullName").html(responseData.responseJSON.fullName);
//                     $("#errorEmail").html(responseData.responseJSON.email);
//                     $("#errorDob").html(responseData.responseJSON.dob);
//                     $("#errorPhone").html(responseData.responseJSON.phone);
//                     $("#errorRole").html(responseData.responseJSON.roleId);
//                     $("#errorStatus").html(responseData.responseJSON.status);
//                 }
//             }
//         });
//     }
// });
