// //导入jQuery
// import "./jquery3.6.0";
document.write("<script src='./jquery3.6.0.js'></script>");
//获取元素
let loveBtn = document.querySelector("#submit");

let adv = ["深情地", "温柔地", "随便地", "紧张地", "幽默地", "滑稽地", "开心地", ""];
//用户点击表白将表白记录显示在表白按钮下面
let record = [];
let i = 0;
getMessages();
loveBtn.onclick = function() {
    //1.获取表白内容
    let inputs = document.querySelectorAll("input");
    let from = inputs[0].value;
    let to = inputs[1].value;
    let message = inputs[2].value;
    //2.如果有一项内天为空，不处理
    if (from == '' || to == '' || message == '') {
        return;
    }
    //3.汇总表白语言
    let n = adv.length;
    let index = (Math.floor(Math.random() * 1000) + 1) % n;
    let romAdv = adv[index];
    let loveMess = from + romAdv + "对" + to + "说" + message;
    record[i] = "留言" + (i+1) + ":" + loveMess;
    //4.新建结点，插入表白记录
    let div = document.createElement("div");
    div.innerHTML = record[i];
    div.className = 'oh';
    let container = document.querySelector(".container");
    container.appendChild(div);
    i++;
    //5.表白完，清空输入框
    for (let j = 0; j < inputs.length; j++) {
        inputs[j].value = '';
    }
    //6.构造ajax请求
    let body = {
        from: from,
        to: to,
        message: message,
        //随机词
        romAdv: adv[index]
    }
    $.ajax({
        type: "post",
        url:"messageDB",
        contentType:"application/json; charset=utf8",
        data:JSON.stringify(body),
        success:function(body){
            alert("表白成功！");
        },
        error:function() {
            alert("表白失败！");
        }
    });
}
function getMessages() {
    $.ajax({
        type: "get",
        url: "messageDB",
        success: function(body) {
            let container = document.querySelector(".container");
            for (message of body) {
                let loveMess = message.from + message.romAdv + "对" + message.to + "说" + message.message;
                record[i] = "留言" + (i+1) + ":" + loveMess;
                let div = document.createElement("div");
                div.innerHTML = record[i];
                div.className = 'oh';
                container.appendChild(div);
                i++;
            }
        },
        error: function () {
            alert("资源获取失败!");
        }
    });
}

