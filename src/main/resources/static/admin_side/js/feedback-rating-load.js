function getRatingByTime(){

    console.log("run");
    let fromDate = document.getElementById("fromDate").value.toISOString();
    let toDate = document.getElementById("toDate").value.toISOString();
    if(fromDate)
    console.log("run");

    $.ajax({
        url: "/public/school-list/school-rating/time",
        type: "post",
        data: {
            districtId: 1,
        },
        success: function (json){
            console.log("load ward" + json);
        },
        error: function (xhr){
            console.log("load ward failed" + xhr);
        },
    });
}

function viewMoreFeedback(nextPage){

}

function searchFeedback(){

}