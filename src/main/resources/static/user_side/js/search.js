document.addEventListener("DOMContentLoaded", function () {
    function handleCheckboxChange() {
        let selectedFacilities = Array.from(document.querySelectorAll('input[name="facilities"]:checked'))
            .map(checkbox => checkbox.value);
        let selectedUtilities = Array.from(document.querySelectorAll('input[name="utilities"]:checked'))
            .map(checkbox => checkbox.value);
    }
    document.querySelectorAll('input[name="facilities"]').forEach(checkbox => {
        checkbox.addEventListener("change", handleCheckboxChange);
    });
    document.querySelectorAll('input[name="utilities"]').forEach(checkbox => {
        checkbox.addEventListener("change", handleCheckboxChange);
    });

});


