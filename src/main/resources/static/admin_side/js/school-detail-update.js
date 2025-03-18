function changeSchoolStatus(role, type, schoolId, recordNo){
    if(schoolId == null){
        schoolId = document.getElementById("schoolId").value;
    }
    if(recordNo == null){
        recordNo = document.getElementById("recordNo").value;
    }
    $.ajax({
        url: role + "/school/" + type,
        type: "post",
        data: {
            schoolId: schoolId,
            recordNo: recordNo,
        },
        success: function (json){
            console.log("load ok " + json);
            location.reload();
        },
        error: function (xhr){
            console.log("load failed " + xhr);
            $('#deleteModel').modal('show');
        },
    });
}

function openEdit(){
    let buttonsTo = ["edit", "submit", "delete", "reject", "approve", "publish", "unpublish"];

    buttonsTo.forEach(id => {
        let button = document.getElementById(id);
        if (button) {
            button.hidden = true;
        }
    });

    buttonsTo = ["cancel", "update"]

    buttonsTo.forEach(id => {
        let button = document.getElementById(id);
        if (button) {
            button.hidden = false;
        }
    });
    enableSelected(true);
    enableSelected(false);
    document.getElementById("image").removeAttribute("disabled");
    document.getElementById("schoolName").removeAttribute("disabled");
    document.getElementById("typeId").removeAttribute("disabled");
    document.getElementById("address").removeAttribute("disabled");
    document.getElementById("city").removeAttribute("disabled");
    document.getElementById("district").removeAttribute("disabled");
    document.getElementById("ward").removeAttribute("disabled");
    document.getElementById("email").removeAttribute("disabled");
    document.getElementById("phone").removeAttribute("disabled");
    document.getElementById("childReceivingAge").removeAttribute("disabled");
    document.getElementById("educationMethod").removeAttribute("disabled");
    document.getElementById("feeTo").removeAttribute("disabled");
    document.getElementById("feeFrom").removeAttribute("disabled");

    quill.enable(true);
    document.getElementById('editor').style.backgroundColor = "white";
}

function updateSchool(){
    let formData = new FormData();
    formData.append("image", document.getElementById("image").files[0]);
    formData.append("id", document.getElementById("schoolId").value);
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
    formData.append("statusId", document.getElementById("status").value);
    formData.append("recordNo", document.getElementById("recordNo").value);

    $.ajax({
        url: "/manager/school/update",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function (json){
            document.getElementById("msg-popup-1").textContent = json.message;
            document.getElementById("button-popup-1").addEventListener("click", function(){
                location.reload();
            });
            var modalElement = document.getElementById('notificationModel');

            var modal = new bootstrap.Modal(modalElement);

            modal.show();
        },
        error: function (xhr){
            console.log(xhr);

            document.getElementById("msg-popup-1").textContent = "Update failed!";

            var modalElement = document.getElementById('notificationModel');

            var modal = new bootstrap.Modal(modalElement);

            modal.show('notificationModel');

        },
    });
}

function getSelected(isFacility) {
    let facilityCheckboxes
    if(isFacility) {
        facilityCheckboxes = document.querySelectorAll('input[name="schoolFacilities"]:checked');
    }
    else {
        facilityCheckboxes = document.querySelectorAll('input[name="schoolUtilities"]:checked');
    }

    const selectedFacilities = [];

    facilityCheckboxes.forEach(checkbox => {
        selectedFacilities.push(checkbox.value);
    });

    return selectedFacilities;
}

function enableSelected(isFacility){
    if(isFacility) {
        facilityCheckboxes = document.querySelectorAll('input[name="schoolFacilities"]');
    }
    else {
        facilityCheckboxes = document.querySelectorAll('input[name="schoolUtilities"]');
    }

    facilityCheckboxes.forEach(checkbox => {
        checkbox.removeAttribute("disabled");
    });
}