function addNewSchool(status, saveType){
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
    formData.append("statusId", status);

    $.ajax({
        url: "/school-owner/school/" + saveType,
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (json){
            console.log(json);
        },
        error: function (xhr){
            console.log("save failed" + xhr);
        },
    });
};