// setTimeout(function () {
//     $(".alert").remove();
// }, 3000); // Hide alert after 3 seconds

$("body").on("submit", "#userForm", function (event) {
    event.preventDefault();

    $(".error-message").html("");
    $(".text-danger").html("");
    var count = 0;
    var phoneRegex = /^[0-9]{10,15}$/;
    var today = new Date().toISOString().split("T")[0];

    if ($("#fullName").val().trim() === '') {
        $("#errorFullName").html('Please enter full name');
        count++;
    }
    if ($("#email").val().trim() === '') {
        $("#errorEmail").html('Please enter email');
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


    if (count === 0) {
        var user = {
            id: $("#userID").val() || null,
            fullName: $("#fullName").val().trim(),
            email: $("#email").val().trim(),
            dob: $("#dob").val().trim(),
            phone: $("#phone").val().trim(),
            role: $('#role').val().trim(),
            status: $("#status").val().trim()


        };
        $.ajax({
            url: "/admin/api/save-user",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(user),
            success: function (response) {
                alert(response.message);
            },
            error: function (xhr) {
                var errors = xhr.responseJSON;
                if (errors) {
                    $("#errorFullName").html(errors.fullName);
                    $("#errorEmail").html(errors.email);
                    $("#errorDob").html(errors.dob);
                    $("#errorPhone").html(errors.phone);
                    $("#errorRole").html(errors.role);
                    $("#errorStatus").html(errors.status);
                }
            }
        });
    }
});

document.getElementById("cancel-button").addEventListener("click", function () {
    var previousUrl = localStorage.getItem('previousUrl');
    if (previousUrl) {
        window.location.href = previousUrl;
        localStorage.removeItem('previousUrl');
    } else {
        window.history.back();
    }
});

