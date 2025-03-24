//load district by city id
function loadDistrict(cityId) {
    $.ajax({
        url: "/public/district",
        type: "get",
        data: { cityId: cityId },
        success: function (json){
            let response = '<option value="" disabled selected>District</option>';
            json.forEach(item => {
                response += '<option value="'+item.id+'">'+item.districtName+'</option>';
            });
            document.getElementById("district").innerHTML = response;
            document.getElementById("district").removeAttribute("disabled");

            let wardElement = document.getElementById("ward");
            wardElement.innerHTML = '<option value="" disabled selected>Ward</option>';
            wardElement.disabled = true;
            wardElement.value = "";
            document.getElementById("address").disabled = true;
            document.getElementById("address").value = "";

        },
        error: function (xhr){
            console.log("Load district failed", xhr);
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
            console.log("load ward failed" + xhr);
        },
    });
}
function enableParam(paramId){
    document.getElementById(paramId).disabled = false;
}
