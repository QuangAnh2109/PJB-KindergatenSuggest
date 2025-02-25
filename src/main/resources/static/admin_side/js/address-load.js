function loadDistrict(cityId) {
    $.ajax({
        url: "/admin/school-form/district",
        type: "get",
        data: {
            cityId: cityId,
        },
        success: function (json){
            response = '<option value="" disabled selected>District</option>';
            json.forEach(item => {
                response += '<option value="'+item.id+'">'+item.districtName+'</option>';
            });
            document.getElementById("district").innerHTML = response;
            document.getElementById("district").removeAttribute("disabled");
            document.getElementById("ward").disabled = true;
            document.getElementById("address").disabled = true;
        },
        error: function (xhr){
            console.log("load district faild" + xhr);
        },
    });
}
function loadWard(districtId) {
    $.ajax({
        url: "/admin/school-form/ward",
        type: "get",
        data: {
            districtId: districtId,
        },
        success: function (json){
            response = '<option value="" disabled selected>Ward</option>';
            json.forEach(item => {
                response += '<option value="'+item.id+'">'+item.wardName+'</option>';
            });
            document.getElementById("ward").innerHTML = response;
            document.getElementById("ward").removeAttribute("disabled");
            document.getElementById("address").disabled = true;
        },
        error: function (xhr){
            console.log("load ward faild" + xhr);
        },
    });
}
function enableParam(paramId){
    document.getElementById(paramId).disabled = false;
}