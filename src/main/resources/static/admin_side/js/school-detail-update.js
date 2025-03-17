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

}