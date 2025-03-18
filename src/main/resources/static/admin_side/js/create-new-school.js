function addNewSchool(status){
        let formData = new FormData();
    formData.append("image", document.getElementById("image").files[0]);
    formData.append("name", document.getElementById("schoolName").value);
    formData.append("typeId", document.getElementById("typeId").value);
    formData.append("address", document.getElementById("address").value);
    formData.append("cityId", document.getElementById("city").value);
    formData.append("districtId", document.getElementById("district").value);
    formData.append("wardId", document.getElementById("ward").value);
    formData.append("email", document.getElementById("email").value);
    formData.append("phone", document.getElementById("phone").value);
    formData.append("childReceivingAgeId", document.getElementById("childReceivingAge").value);
    formData.append("educationMethodId", document.getElementById("educationMethod").value);
    formData.append("feeTo", document.getElementById("feeTo").value);
    formData.append("feeFrom", document.getElementById("feeFrom").value);
    formData.append("introduction", document.getElementById("editor").innerHTML);
    formData.append("schoolFacilities", getSelected(true));
    formData.append("schoolUtilities", getSelected(false));
    formData.append("statusId", status);

    $.ajax({
        url: "/school-owner/school/add-new",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function (json){
            console.log("ok");
            console.log(json);
            document.getElementById("msg-popup-1").textContent = json.message;
            document.getElementById("button-popup-1").addEventListener("click", function(){
                window.location.href = "/manager/school/view-detail?id=" + json.id;
            });
            var modalElement = document.getElementById('notificationModel');

            var modal = new bootstrap.Modal(modalElement);

            modal.show();
        },
        error: function (xhr){
            console.log("error");
            console.log(xhr);

            document.getElementById("msg-popup-1").textContent = "Failed!";

            var modalElement = document.getElementById('notificationModel');

            var modal = new bootstrap.Modal(modalElement);

            modal.show('notificationModel');

        },
    });
};

function getSelected(isFacility) {
    // Lấy tất cả các checkbox có name là "schoolFacilities" đã được chọn
    let facilityCheckboxes
    if(isFacility) {
        facilityCheckboxes = document.querySelectorAll('input[name="schoolFacilities"]:checked');
    }
    else {
        facilityCheckboxes = document.querySelectorAll('input[name="schoolUtilities"]:checked');
    }

    // Tạo mảng chứa các giá trị đã chọn
    const selectedFacilities = [];

    // Lặp qua tất cả các checkbox đã chọn và lấy giá trị
    facilityCheckboxes.forEach(checkbox => {
        selectedFacilities.push(checkbox.value);
    });

    return selectedFacilities;
}