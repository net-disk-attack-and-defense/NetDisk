function popout_RSU() {
    const popout = document.getElementById("popout_operate");
    popout.classList.toggle('active')
}

function popout_RCU() {
    const popout = document.getElementById("popout_user");
    popout.classList.toggle('active');
    sessionStorage.setItem("delid",event.srcElement.parentElement.lastElementChild.innerHTML);
    sessionStorage.setItem("delid_name",event.srcElement.parentElement.children[1].innerHTML);
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

function submitid() {
    var delid = sessionStorage.getItem("delid");
    document.getElementById("bt_del").value = delid;
}

function submitid_File() {
    var delid = sessionStorage.getItem("delid");
    var delid_name = sessionStorage.getItem("delid_name"); /*因为查看文件时最好显示用户名，因此多出一个*/
    document.getElementById("bt_ShowFile").value = delid;
    document.getElementById("bt_ShowFileName").value = delid_name;
}