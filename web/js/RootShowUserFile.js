function popoutToggle() {
    const popout = document.getElementById("popout_upload");
    popout.classList.toggle('active')
}

function submit() {
    document.getElementById("DoOrDe").submit();
}

function changeAction(x) {
    if (x.getAttribute("class") === "bt_download"){
        document.getElementById("DoOrDe").getAttributeNode("action").value="RFDown";
    }else {
        document.getElementById("DoOrDe").getAttributeNode("action").value="RFD";
    }
}