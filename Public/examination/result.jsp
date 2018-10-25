<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%
	double postAbility = (double) request.getAttribute("postAbility");
	if (postAbility < -3.0) {
		postAbility = -3.0;
	} else if (postAbility > 3.0) {
		postAbility = 3.0;
	}
	//highCharts(startを0)に対応するため、変換
	postAbility = postAbility + 3.0;
%>
<%
	String level = null;
	if (postAbility < 1.5) {
		level = "レベル1";
	} else if (postAbility >= 1.5 && postAbility < 3.0) {
		level = "レベル2";
	} else if (postAbility >= 3.0 && postAbility < 4.5) {
		level = "レベル3";
	} else if (postAbility >= 4.5 && postAbility < 6.0) {
		level = "レベル4";
	}
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="utf-8">
<title>テスト結果画面</title>
<link href="../common/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="../common/css/original.css" rel="stylesheet" />
</head>

<body>
	<div class="container">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<nav class="navbar navbar-default navbar-fixed-top" id="navbar_overall_home">
				<div class="navbar-header">
					<a class="navbar-brand" id="navbar_text_left_side"><font size="+2">情報モラル適応型テスト</font></a>
				</div>
			</nav>

			<div class="row" style="padding: 45px 0 0 0">
				<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					<div class="page-header" style="margin-top: -20px; padding-bottom: 0px;">
						<h1>
							結果 <small> 受検時間 <script>
								//localStrageに保存された受検時間を取得
								var sec = Number(window.localStorage
										.getItem('secLocalStrage'));
								var min = Number(window.localStorage
										.getItem('minLocalStrage'));
								var hour = Number(window.localStorage
										.getItem('hourLocalStrage'));
								// 0埋め
								secNumber = ('0' + sec).slice(-2);
								minNumber = ('0' + min).slice(-2);
								hourNumber = ('0' + hour).slice(-2);
								//	受検時間の表示
								document.write(hourNumber + ':' + minNumber
										+ ':' + secNumber);
								//localStrageに保存されている値をクリアにする．
								window.localStorage.clear();
							</script>
							</small>
						</h1>
					</div>
					<h2>
						あなたの情報モラルレベルは<span style="font-size: xx-large;">&nbsp;<span style="color: #FF1493;"> <%=level%></span></span>&nbsp;です。
					</h2>
					<div id="chart" style="min-width: 310px; max-width: 100%; height: 300px; margin: 0 auto"></div>
					<div class="well well-sm">
						グラフの<span style="background-color: #FF3333">&nbsp;&nbsp;&nbsp;</span>はレベル1、<span style="background-color: #FFFF66">&nbsp;&nbsp;&nbsp;</span>はレベル2、<span
							style="background-color: #33CC00">&nbsp;&nbsp;&nbsp;</span>はレベル3、<span style="background-color: #3366FF">&nbsp;&nbsp;&nbsp;</span>はレベル4にそれぞれ対応しています。
					</div>
					<br>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="../common/bootstrap/js/bootstrap.min.js"></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script type="text/javascript">
	Highcharts.chart('chart', {
		chart : {
			borderColor : '#000000',//線の色
			borderWidth : 0,//外枠の太さ
			type : 'bar',//グラフのタイプ
			//plotBackgroundColor: '#ff4325',//バックグランドカラー
			plotBorderWidth : 0,
			plotBorderColor : '#000000',
		//backgroundColor:'#435210'//全ての要素の背景色
		},
		colors : [ '#33BBFF' ],
		title : {
			text : '',//情報モラル力
			//floating: true,
			floating : true,
			style : {
				color : '#12f342',
				fontSize : '36px'
			}
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : [ '' ],//グラフの一つの値の名前
			lineWidth : 0,//開始線の太さ
			tickColor : '#FF0000',//x軸の補助線の色
			tickWidth : 0,//y軸の補助線の太さ
			title : {
				text : null
			},

		},
		yAxis : {
			min : 0,
			max : 6,
			gridLineColor : '#000000',//線の色
			gridLineWidth : 1,//線の太さ
			//tickInterval: 0.5,
			title : {
				text : null,
			},
			labels : {
				enabled : false
			//true//x軸上のメモリが表示されるか否か。
			},
			plotBands : [ {//グラフの背景色を変化することができる
				color : '#FF3333',
				from : 0.0,
				to : 1.5,
			}, {
				color : '#FFFF66',
				from : 1.5,
				to : 3.0,
			}, {
				color : '#33CC00',
				from : 3.0,
				to : 4.5,
			}, {
				color : '#3366FF',
				from : 4.5,
				to : 6.0
			} ],
		},

		tooltip : {//グラフを表示したい際に，何かが出てくる．
			backgroundColor : '#000000',
			enabled : false
		},

		plotOptions : {
			bar : {
				dataLabels : {
					enabled : false
				}
			}
		},

		legend : {//凡例について
			enabled : false,//表示するか否か。
		},
		credits : {
			enabled : false
		//highchatのお金を支払う画面に移動する．
		},
		exporting : {//画像保存ボタンの非表示
			enabled : false
		},
		series : [ {
			data : [
<%=postAbility%>
	]
		} ]
	});
</script>

<script type="text/javascript">
	//ページが読みこまれた後に，実行する処理
	$(window).load(function() {
		//0.01秒後に処理を実行
		setInterval(function() {
			//URLにハッシュを付ける
			location.hash = "hash";
		}, 10);
	});
</script>
</html>