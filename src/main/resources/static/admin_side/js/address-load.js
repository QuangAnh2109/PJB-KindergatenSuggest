//load district by city id
function loadDistrict(cityId) {
    $.ajax({
        url: "/public/district",
        type: "get",
        data: {
            cityId: cityId,
        },
        success: function (json){
            //change response data to html
            response = '<option value="" disabled selected>District</option>';
            json.forEach(item => {
                response += '<option value="'+item.id+'">'+item.districtName+'</option>';
            });
            //add html to district form
            document.getElementById("district").innerHTML = response;
            document.getElementById("district").removeAttribute("disabled");
            //disable ward form and address form
            document.getElementById("ward").disabled = true;
            document.getElementById("ward").value = "";
            document.getElementById("address").disabled = true;
            document.getElementById("address").value = "";
        },
        error: function (xhr){
            console.log("load district faild" + xhr);
        },
    });
}
//load ward by district id
function loadWard(districtId) {
    $.ajax({
        url: "/public/ward",
        type: "get",
        data: {
            districtId: districtId,
        },
        success: function (json){
            //change response data to html
            response = '<option value="" disabled selected>Ward</option>';
            json.forEach(item => {
                response += '<option value="'+item.id+'">'+item.wardName+'</option>';
            });
            //add html to ward form
            document.getElementById("ward").innerHTML = response;
            document.getElementById("ward").removeAttribute("disabled");
            //disable address form
            document.getElementById("address").disabled = true;
            document.getElementById("address").value = "";
        },
        error: function (xhr){
            console.log("load ward faild" + xhr);
        },
    });
}
function enableParam(paramId){
    document.getElementById(paramId).disabled = false;
}