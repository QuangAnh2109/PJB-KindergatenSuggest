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
    formData.append("introduction", quill.root.innerHTML);
    formData.append("schoolUtilities", getSelected(false));
    formData.append("statusId", status);
    document.getElementById("schoolName-error").style.display = 'none';
    document.getElementById("type-error").style.display = 'none';
    document.getElementById("address-error").style.display = 'none';
    document.getElementById("city-error").style.display = 'none';
    document.getElementById("district-error").style.display = 'none';
    document.getElementById("ward-error").style.display = 'none';
    document.getElementById("email-error").style.display = 'none';
    document.getElementById("phone-error").style.display = 'none';
    document.getElementById("childReceivingAge-error").style.display = 'none';
    document.getElementById("educationMethod-error").style.display = 'none';
    document.getElementById("feeTo-error").style.display = 'none';
    document.getElementById("feeFrom-error").style.display = 'none';
    document.getElementById("fee-error").style.display = 'none';
    document.getElementById("introduction-error").style.display = 'none';
    document.getElementById("image-error").style.display = 'none';

    $.ajax({
        url: "/school-owner/school/add-new",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        dataType: 'json',
        success: function (json){
            document.getElementById("msg-popup-1").textContent = json.message;
            document.getElementById("button-popup-1").addEventListener("click", function(){
                window.location.href = "/manager/school/view-detail?id=" + json.id;
            });
            var modalElement = document.getElementById('notificationModel');

            var modal = new bootstrap.Modal(modalElement);

            modal.show();
        },
        error: function (xhr){
            if(xhr.status === 422){
                if(xhr.responseJSON.name != null){
                    var nameError = document.getElementById("schoolName-error");
                    nameError.textContent = xhr.responseJSON.name;
                    nameError.style.display = 'block';
                }
                if(xhr.responseJSON.schoolType != null){
                    var typeIdError = document.getElementById("type-error");
                    typeIdError.textContent = xhr.responseJSON.schoolType;
                    typeIdError.style.display = 'block';
                }
                if(xhr.responseJSON.address != null){
                    var addressError = document.getElementById("address-error");
                    addressError.textContent = xhr.responseJSON.address;
                    addressError.style.display = 'block';
                }
                if(xhr.responseJSON.city != null){
                    var cityError = document.getElementById("city-error");
                    cityError.textContent = xhr.responseJSON.city;
                    cityError.style.display = 'block';
                }
                if(xhr.responseJSON.district != null){
                    var districtError = document.getElementById("district-error");
                    districtError.textContent = xhr.responseJSON.district;
                    districtError.style.display = 'block';
                }
                if(xhr.responseJSON.ward != null){
                    var wardError = document.getElementById("ward-error");
                    wardError.textContent = xhr.responseJSON.ward;
                    wardError.style.display = 'block';
                }
                if(xhr.responseJSON.email != null){
                    var emailError = document.getElementById("email-error");
                    emailError.textContent = xhr.responseJSON.email;
                    emailError.style.display = 'block';
                }
                if(xhr.responseJSON.phone != null){
                    var phoneError = document.getElementById("phone-error");
                    phoneError.textContent = xhr.responseJSON.phone;
                    phoneError.style.display = 'block';
                }
                if(xhr.responseJSON.childReceivingAge != null){
                    var childReceivingAgeError = document.getElementById("childReceivingAge-error");
                    childReceivingAgeError.textContent = xhr.responseJSON.childReceivingAge;
                    childReceivingAgeError.style.display = 'block';
                }
                if(xhr.responseJSON.educationMethod != null){
                    var educationMethodError = document.getElementById("educationMethod-error");
                    educationMethodError.textContent = xhr.responseJSON.educationMethod;
                    educationMethodError.style.display = 'block';
                }
                if(xhr.responseJSON.feeTo != null){
                    var feeToError = document.getElementById("feeTo-error");
                    feeToError.textContent = xhr.responseJSON.feeTo;
                    feeToError.style.display = 'block';
                }
                if(xhr.responseJSON.feeFrom != null){
                    var feeFromError = document.getElementById("feeFrom-error");
                    feeFromError.textContent = xhr.responseJSON.feeFrom;
                    feeFromError.style.display = 'block';
                }
                if(xhr.responseJSON.fee != null){
                    var feeError = document.getElementById("fee-error");
                    feeError.textContent = xhr.responseJSON.fee;
                    feeError.style.display = 'block';
                }
                if(xhr.responseJSON.introduction != null){
                    var introductionError = document.getElementById("introduction-error");
                    introductionError.textContent = xhr.responseJSON.introduction;
                    introductionError.style.display = 'block';
                }
                if(xhr.responseJSON.image != null){
                    var schoolFacilitiesError = document.getElementById("image-error");
                    schoolFacilitiesError.textContent = xhr.responseJSON.image;
                    schoolFacilitiesError.style.display = 'block';
                }
            }
            else{
                document.getElementById("msg-popup-1").textContent = xhr.responseJSON.message;

                var modalElement = document.getElementById('notificationModel');

                var modal = new bootstrap.Modal(modalElement);

                modal.show('notificationModel');
            }
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