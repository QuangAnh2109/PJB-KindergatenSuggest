function submitSchool(isAdmin){
    let link = "";
    if(isAdmin) link = "/admin"
    else link = "/school-owner"
    $.ajax({
        url: link + "/school/submit",
        type: "post",
        data: {
            schoolId: document.getElementById("schoolId").value,
            recordNo: document.getElementById("recordNo").value,
        },
        success: function (json){
            console.log("load ok" + json);
        },
        error: function (xhr){
            console.log("load failed" + xhr);
        },
    });
};

function deleteSchool(){

};

function rejectSchool(){

};

function approveSchool(){

};

function publishSchool(){

};

function unpublishSchool(){

};

function openEdit(){

}