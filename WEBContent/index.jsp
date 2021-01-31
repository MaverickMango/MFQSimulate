<%@page import="bean.ReadyQueue"%>
<%@page import="java.util.Vector"%>
<%@page import="bean.PCB"%>
<%@page import="bean.LinkedQueue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>多级反馈队列调度算法模拟</title>
    <link rel="stylesheet" href="css/style.css">
</head>

<body>
    <div class="showTime">
        <p>
            <label>当前时间：</label>
            <span class="timer">0</span>
        </p>
        <a href="javascript:sendRefreshRequest();">重置</a>
    </div>
    <div class="createProcess">
        <label>到达时间：</label><input type="text" name="startTime" required>
        <label>运行时间：</label><input type="text" name="lifeCycle" required>
        <a href="javascript:sendCreateRequest();">创建进程</a>
        <div id="info"></div>
    </div>
    <!-- 当前进程同步显示 -->
    <div style="align-items: flex-start;">
        <div class="pTable">
            <table>
                <tr>
                    <td colspan="6">全部已创建进程</td>
                </tr>
                <tr>
                    <th>进程标识</th>
                    <th>到达时间</th>
                    <th>预计服务时间</th>
                    <th>执行所在队列</th>
                    <th>剩余服务时间</th>
                    <th>撤销进程</th>
                </tr>
                <tbody id="p_tbody">
                </tbody>
            </table>
        </div>
        <div class="table">
            <table>
                <tr>
                    <td colspan="6">结束和已撤销进程</td>
                </tr>
                <tr>
                    <th>进程标识</th>
                    <th>开始时间</th>
                    <th>预计服务时间</th>
                    <th>结束就绪队列</th>
                    <th>剩余服务时间</th>
                    <th>结束时间</th>
                </tr>
                <span></span>
                <tbody id="tbody">
                </tbody>
            </table>
        </div>
    </div>
    <div class="show">
        <input type="range" id="scale_range" min="0.649" max="1.0" value="1.0" step="0.001"><!-- 0.649  0.351 -->
        <div id="img">
            <canvas id="processing" width="2000" height="1000"></canvas>
        </div>
    </div>
    <!-- </div> -->
    <div class="footer">
        <div class="container">
            <span></span>
            <footer>Copyright &copy; 2020 Mengjiao Liu . Muti-Stage Feedback Queue Scheduling Algorithm</footer>
        </div>
    </div>
</body>

<script>
    //画布放缩
    var canvas = document.getElementById("processing");
    var slider = document.getElementById("scale_range");
    var scale = slider.value;
    canvas.style.width = canvas.width * scale + "px";
    canvas.style.height = canvas.height * scale + "px";
    // console.log(canvas.style.width + " " + canvas.style.height);
    slider.onmousemove = function () {
        scale = slider.value;
        canvas.style.width = canvas.width * scale + "px";
        canvas.style.height = canvas.height * scale + "px";
        //console.log(slider.value);
        slider.setAttribute("style", "background-size:" + (((this.value - 0.649) / 0.351) * 100) + "% 100%;");
    }

    //ajax
    var xmlHttp = "";
    var sXmlHttp1 = "";//每秒提交的请求
    var sXmlHttp2 = "";
    function createXMLHttpRequest(x) {
        if (window.ActiveXObject) {//如果是IE
            x = new ActiveXObject("Microsoft.XMLHTTP");
        } else if (window.XMLHttpRequest) {//非IE
            x = new XMLHttpRequest();
        }
        return x;
    }

    var startTime = document.getElementsByName("startTime")[0];
    var lifeCycle = document.getElementsByName("lifeCycle")[0];
    var input = document.getElementsByTagName("input");//0是到达时间 1是服务时间
    var info = document.getElementById("info");

    var pageTimer = "";//页面计时器
        var timeCount = <%=application.getAttribute("timer").toString() %>
    var timer = document.getElementsByClassName("timer")[0];
        function tick() {
            //页面定时任务

            //计时器
            timeCount++;
            timer.innerText = timeCount;

            //写入结束进程 请求查找结束队列
            sendCheckOverRequest();
            //画图
            sendGetExecutingRequest();
        }
        function startTimer() {
            clearInterval(pageTimer);
            pageTimer = setInterval(tick, 1000);
        }
        startTimer();

    //向结束列表写入进程
    function writeTable(result) {
        var tbody = document.getElementById("tbody");
        for (var i = 0; i < result.length; i++) {
            var tr = document.createElement("tr");
            var td0 = document.createElement("td");
            td0.innerHTML = result[i].pid;
            var td1 = document.createElement("td");
            td1.innerHTML = result[i].startTime;
            var td2 = document.createElement("td");
            td2.innerHTML = result[i].serveTime;
            var td3 = document.createElement("td");
            td3.innerHTML = result[i].priority;
            var td4 = document.createElement("td");
            td4.innerHTML = result[i].lifeCycle;
            var td5 = document.createElement("td");
            td5.innerHTML = result[i].endTime;
            tr.appendChild(td0);
            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            tr.appendChild(td5);
            tbody.appendChild(tr);
        }
    }

    //查找结束进程
    function sendCheckOverRequest() {
        sXmlHttp1 = createXMLHttpRequest(sXmlHttp1);
        var url = "CheckOverServlet?current=" + (new Date().getTime());
        sXmlHttp1.open("GET", url);
        sXmlHttp1.onreadystatechange = appendOverProcess;
        sXmlHttp1.send();
    }
    function appendOverProcess() {
        if (sXmlHttp1.readyState == 4 && sXmlHttp1.status == 200) {
            var result = eval(sXmlHttp1.responseText);
            writeTable(result);
        }
    }

    //画图
    var canv = document.getElementById("processing");
    var context = canv.getContext("2d");
    function initDraw() {
        context.strokeStyle = "rgb(23,162,184)";
        context.fillStyle = "rgb(23,162,184)";
        context.beginPath();
        context.moveTo(0, 0);
        context.lineTo(0, 1000);
        context.lineTo(2000, 1000);
        //context.moveTo(0, 0);
        context.lineTo(2000, 0);
        context.lineTo(0, 0);
        context.moveTo(60, 990);
        context.lineTo(10, 990);
        context.lineTo(10, 940);
        context.fillText("时间", 40, 980);
        context.fillText("进程名", 20, 950)
        for (var i = 0; i < 100; i++) {
            context.moveTo(i * 100, 0);
            context.lineTo(i * 100, 10);
            context.fillText(i * 10, i * 100 + 10, 15);/* i*100 */
        }
        context.stroke();
        context.closePath();
    }
    initDraw();

    function paintName(pid) {
        context.strokeStyle = "rgb(23,162,184)";
        context.beginPath();
        context.moveTo(0, pid * 50 + 50);/*pid * 100 + 100*/
        context.lineTo(10, pid * 50 + 50);
        context.stroke();
        context.closePath();
        context.fillStyle = "rgb(23,162,184)";
        context.fillText(pid, 20, pid * 50 + 50);
        //context.fillText(timeCount, 20, timeCount*10 - 100);
    }

    //请求刷新:画图/刷新剩余时间
    function sendGetExecutingRequest() {
        sXmlHttp2 = createXMLHttpRequest(sXmlHttp2);
        var url = "GetExecutingServlet?current=" + (new Date().getTime());
        sXmlHttp2.open("GET", url);
        sXmlHttp2.onreadystatechange = change;
        sXmlHttp2.send();
    }
    function change() {
        if (sXmlHttp2.readyState == 4) {
            if (sXmlHttp2.status == 200) {
                var result = eval(sXmlHttp2.responseText);
                if (result.length != 0) {
                    //console.log(result);
                    setLifeCycle(result);
                    draw(result);
                }
            }
        }
    }
    //画图
    function draw(result) {
        for (var i = 0; i < result.length; i++) {
            var id = result[i].pid;
            context.strokeStyle = "rgb(253, 243, 243)";
            context.beginPath();
            context.moveTo((timeCount + 1) * 10, id * 50 + 50);/* id*100+100 */
            context.lineTo(timeCount * 10, id * 50 + 50);
            context.closePath();
            context.stroke();
        }
    }
    //动态刷新现有进程列表
    function setLifeCycle(result) {
        var ptbody = document.getElementById("p_tbody");
        var trs = document.getElementsByTagName("tr");
        for (var i = 0; i < trs.length; i++) {
            var td0 = trs[i].childNodes[0];
            for (var j = 0; j < result.length; j++) {
                if (td0.innerHTML == result[j].pid) {
                    var td3 = trs[i].childNodes[3];
                    td3.innerHTML = result[j].priority;
                    var td4 = trs[i].childNodes[4];
                    td4.innerHTML = result[j].lifeCycle - 1;
                    //if(td4.innerHTML == 0) {
                    //	trs[i].remove();
                    //	writeTable(result);
                    //}
                }
            }
        }
    }

    //提交创建进程请求
    function sendCreateRequest() {
        if (startTime.value != "" && lifeCycle.value != "") {
            if (parseInt(startTime.value) > parseInt(timer.innerText)) {
                xmlHttp = createXMLHttpRequest(xmlHttp);
                var url = "CreateProcessServlet?current=" + (new Date().getTime())
                    + "&startTime=" + startTime.value + "&lifeCycle=" + lifeCycle.value;;
                xmlHttp.open("GET", url);//建立连接 默认异步处理
                xmlHttp.onreadystatechange = refreshAndShow;//状态改变事件
                xmlHttp.send();//发起请求
            } else {
                info.innerText = "到达时间小于当前时间：不允许创建该进程！";
                input[0].value = "";
                input[1].value = "";
            }
        } else {
            info.innerText = "不允许提交空白项！";
        }
    }
    function refreshAndShow() {
        if (xmlHttp.readyState == 4) {
            startTime.value = "";
            lifeCycle.value = "";
            if (xmlHttp.status == 200) {
                info.innerText = "";
                var ptbody = document.getElementById("p_tbody");
                var result = eval(xmlHttp.responseText);
                for (var i = 0; i < result.length; i++) {
                    var tr = document.createElement("tr");
                    var td0 = document.createElement("td");
                    td0.innerHTML = result[i].pid;
                    //添加画布中的进程
                    paintName(result[i].pid);
                    var td1 = document.createElement("td");
                    td1.innerHTML = result[i].startTime;
                    var td2 = document.createElement("td");
                    td2.innerHTML = result[i].serveTime;
                    var td3 = document.createElement("td");
                    td3.innerHTML = result[i].priority;
                    var td4 = document.createElement("td");
                    td4.innerHTML = result[i].lifeCycle;
                    var td5 = document.createElement("td");
                    td5.innerHTML = "<button onclick='undoProcessRequest(this);'>撤销</button>";
                    tr.appendChild(td0);
                    tr.appendChild(td1);
                    tr.appendChild(td2);
                    tr.appendChild(td3);
                    tr.appendChild(td4);
                    tr.appendChild(td5);
                    ptbody.appendChild(tr);
                }
            }
        }
    }
    //撤销进程请求
    function undoProcessRequest(obj) {
        xmlHttp = createXMLHttpRequest(xmlHttp);
        var pid = obj.parentNode.parentNode.firstChild.innerText;
        var url = "UndoProcessServlet?current=" + (new Date().getTime()) + "&pid=" + pid;
        xmlHttp.open("GET", url);
        xmlHttp.onreadystatechange = undoProcess;
        xmlHttp.send();

        obj.parentNode.parentNode.remove();
    }
    function undoProcess() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            //console.log(xmlHttp.responseText);
            var result = eval(xmlHttp.responseText);
            if (result.length == 0) {
                //console.log("当前进程已运行完毕！");
                info.innerText = "当前进程已运行完毕！";
            } else {
            	info.innerText = "";
                writeTable(result);
            }
        }
    }
    //提交重置请求
    function sendRefreshRequest() {
        xmlHttp = createXMLHttpRequest(xmlHttp);
        var url = "ResetTimeServlet?current=" + (new Date().getTime());
        xmlHttp.open("GET", url);
        xmlHttp.onreadystatechange = refreshTime;
        xmlHttp.send();
    }
    function refreshTime() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            //重置时间
            timeCount = xmlHttp.responseText;
            //清空两张表
            var tbody = document.getElementById("tbody");
            while (tbody.childNodes.length > 0) {
                tbody.removeChild(tbody.childNodes[0]);
            }
            var ptbody = document.getElementById("p_tbody");
            while (ptbody.childNodes.length > 0) {
                ptbody.removeChild(ptbody.childNodes[0]);
            }
            //清空输入框
            input[0].value = "";
            input[1].value = "";
            //清空提示信息
            info.innerText = "";
            //重置画布
            context.clearRect(0, 0, 2000, 1000);
            initDraw();
            //重启计时器
            startTimer();
        }
    }
</script>

</html>