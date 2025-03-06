
    $(document).ready(function () {
        // Catch event EnterKey and button Search
        $("#keyword").keypress(handleSearchEvent);
        $("#searchButton").click(handleSearchEvent);

        function handleSearchEvent(event) {
            if (event.type === "click" || event.which === 13) {
                event.preventDefault();
                searchSchools();
            }
        }

        // Ajax process,
        function searchSchools() {
            let keyword = $("#keyword").val();
            let cityId = $("#citySelect").val();
            let districtId = $("#district").val();

            $.ajax({
                url: "/public/search/ajax",
                type: "GET",
                data: { keyword: keyword, cityId: cityId, districtId: districtId },
                success: function (data) {
                    $("#results").html("");
                    if (data.length === 0) {
                        $("#results").html("<h3>No schools found.</h3>");
                    } else {
                        let html = '<h3>Found ' + data.length + ' schools</h3>';
                        html += '<div class="row">';
                        data.forEach(school => {
                            html += '<div class="col-lg-4">' +
                                '<div class="card school-card">' +
                                '<div class="card-body">' +
                                '<h5 class="school-title">' + school.name + '</h5>' +
                                '<p>City: ' + school.city.name + '</p>' +
                                '<p>District: ' + school.district.name + '</p>' +
                                '<p>Tuition Fee: ' + school.tuitionFee + ' VND</p>' +
                                '<a href="#" class="btn-primary">View Details</a>' +
                                '</div></div></div>';
                        });
                        html += '</div>';
                        $("#results").html(html);
                    }
                },
                error: function () {
                    $("#results").html("<h3>Error loading search results.</h3>");
                }
            });
        }
    });