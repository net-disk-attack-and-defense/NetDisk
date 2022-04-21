function popoutToggle() {
    const popout = document.getElementById("popout_upload");
    popout.classList.toggle('active')
}

function changeAction(x) {
    if (x.getAttribute("class") === "bt_download"){
        document.getElementById("DoOrDe").getAttributeNode("action").value="FileDownload";
    }else {
        document.getElementById("DoOrDe").getAttributeNode("action").value="FileDelete";
    }
}