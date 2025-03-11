function loadSchoolInPage(nextPage, isAdmin){
    url = "";
    search = document.getElementById("search").value;
    if(isAdmin){
        url = "/admin";
    }else{
        url = "/school-owner";
    }
    $.ajax({
        url: url + "/school-list/searchAndPaging",
        type: "GET",
        data: {
            search: search,
            page: nextPage,
        },
        success: function (data) {
            console.log(data);
            document.getElementById("main-content-school-table").outerHTML = data;
            updateUrlParams(search, nextPage)
            // newTable = document.getElementById("search").value;
            // statusDict = {};
            // data.status.forEach(sta => {
            //     statusDict[sta.typeKey] = sta.typeValue;
            // });
            //
            // data.pageSchool.content.forEach(school => {
            //     newTable += "<tr style=\"height: 80px\">\n" +
            //         "                <th scope=\"row\">" + school.schoolName + "</th>\n" +
            //         "                <td >" + school.schoolAddress + "</td>\n" +
            //         "                <td >" + school.schoolPhone + "</td>\n" +
            //         "                <td >" + school.schoolEmail + "</td>\n" +
            //         "                <td >" + convertUTCToLocal(school.postedDate) + "</td>\n" +
            //         "                <td >" + statusDict[school.statusId] + "</td>\n" +
            //         "                <td class=\"row\">\n";
            //     if (school.statusId != 7) {
            //         newTable += "<a href=\"" + url + "/school/detail/" + school.schoolId + "&true\" ><h4 class=\"col mdi mdi-file-document-edit-outline\"></h4></a>";
            //     }
            //     if (school.canDelete) {
            //         newTable += "<a onclick='deleteSchool(" + school.schoolId + ")' ><h4 class=\"col mdi mdi-delete-outline\"></h4></a>";
            //     }
            //     newTable += "</td></tr>";
            // });
            // pageNumber = data.pageSchool.pageable.pageNumber;
            // pageSize = data.pageSchool.totalPages;
            // newPageBar = "<nav aria-label=\"Page navigation\">\n" +
            //     "                    <ul class=\"pagination justify-content-end\">";
            // for (let i = 0; i < pageSize; i++) {
            //     if(i==pageNumber) newPageBar+= "<li class=\"page-item active\">";
            //     else newPageBar+= "<li class=\"page-item\">";
            //     newPageBar += "     <a class=\"page-link button-search\"\n" +
            //         "               onclick=\"loadSchoolInPage(" + (pageNumber-1) + "," + isAdmin + ")\"\n" +
            //         "               >"+(i+1)+"</a>\n" +
            //         "          </li>";
            // }
            // newPageBar += "</ul></nav>";
            // document.getElementById("page-bar").innerHTML = newPageBar;
            // document.getElementById("school-table").innerHTML = newTable;
        }
    });
}

function loadSchoolListForSearch(isAdmin) {
    url = "/school-list/" + document.getElementById("search").value + "&0";
    if (isAdmin) {
        url = "/admin" + url;
    } else {
        url = "/school-owner" + url;
    }
    $.ajax({
        url: "/api/schools",
        type: "GET",
        success: function (data) {
            var schools = data.data;
            var schoolList = $("#school-list");
            schoolList.empty();
            for (var i = 0; i < schools.length; i++) {
                var school = schools[i];
                var schoolItem = $("<li class='school-item'></li>");
                var schoolLink = $("<a href='/school/" + school.id + "'></a>");
                schoolLink.text(school.name);
                schoolItem.append(schoolLink);
                schoolList.append(schoolItem);
            }
        }
    });
}

function updateUrlParams(search, currentPage) {
    let newUrl = window.location.pathname + "?search=" + encodeURIComponent(search) + "&currentPage=" + currentPage;
    window.history.pushState({ path: newUrl }, "", newUrl);
}