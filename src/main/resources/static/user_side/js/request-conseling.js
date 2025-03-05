let counselingModalObj;

document.addEventListener('DOMContentLoaded', function () {
    counselingModalObj = new bootstrap.Modal(document.getElementById('counselingModal'));
});

function openCounselingForm() {
    document.getElementById('counselingForm').reset();
    counselingModalObj.show();
}

function submitCounselingForm() {
    const fullName = document.getElementById('fullName').value;
    const email = document.getElementById('email').value;
    const mobile = document.getElementById('mobile').value;
    const inquiries = document.getElementById('inquiries').value;

    if (!fullName || !email || !mobile) {
        alert('Please fill full information!');
        return;
    }

    console.log('Sending data:', {fullName, email, mobile, inquiries});

    alert('My request is sent successfully!');

    counselingModalObj.hide();
}