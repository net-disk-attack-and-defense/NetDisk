function popout_RSU() {
    const popout = document.getElementById("popout_operate");
    popout.classList.toggle('active')
}

function popout_RCU() {
    const popout = document.getElementById("popout_user");
    popout.classList.toggle('active')
}

function ShowBgColor() {
    if(event && event.srcElement && event.srcElement.tagName=="TD"){
        var tr = event.srcElement.parentElement;
        tr.style.backgroundColor = "lightcyan";
    }
}

function ClearBgColor() {
    if(event && event.srcElement && event.srcElement.tagName=="TD"){
        var tr = event.srcElement.parentElement;
        tr.style.backgroundColor = null;
    }
}