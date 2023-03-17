<template>
	<div class="memberMain">
		<div class="mftcc-details-card" style="padding:20px;min-height:50px">
			<el-row>
				<el-col :span="10">
					<div class="block memImg">
						<el-avatar :size="126" :src="url"></el-avatar>
						<el-row>
							<el-col :span="6"><i class="menICon1"></i></el-col>
							<el-col :span="6"><i class="menICon2"></i></el-col>
							<el-col :span="6"><i class="menICon3"></i></el-col>
							<el-col :span="6"><i class="menICon4"></i></el-col>
						</el-row>
					</div>
					<div class="block memTit">
						<span>张王李赵</span>

						<ul>
							<li>
								<i class="memTit1"  ></i>
								<b>微金时代</b>
							</li>
							<li>
								<i class="memTit2"></i>
								<b>高级领导</b>
							</li>
							<li>
								<i class="memTit3"></i>
								<b>18696286001</b>
							</li>
						</ul>
					</div>
				</el-col>
				<el-col :span="14" class="memData">
					<el-row>
						<el-col :span="6">
							<span>
								<i class="memData1"></i>
								<b>成交笔数（笔）</b>
							</span>
							<h5>27</h5>
						</el-col>
						<el-col :span="6">
							<span>
								<i class="memData2"></i>
								<b>成交金额（万）</b>
							</span>
							<h5>600.00</h5>
						</el-col>
						<el-col :span="6">
							<span>
								<i class="memData3"></i>
								<b>在履行笔数（笔）</b>
							</span>
							<h5>8</h5>
						</el-col>
						<el-col :span="6">
							<span>
								<i class="memData4"></i>
								<b>待还金额（万）</b>
							</span>
							<h5>100.05</h5>
						</el-col>
					</el-row>
				</el-col>
			</el-row>
		</div>
		<div class="mftcc-details-card messageCenter" style="min-height:50px">
			<h4>消息中心</h4>
			<ul>
				<li>
					<p>
						<i class="memMess1"></i>
						<span>通知公告</span>
					</p>
					<b>17</b>
				</li>
				<li>
					<p>
						<i class="memMess2"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess3"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess4"></i>
						<span>通知公告</span>
					</p>
					<b>6</b>
				</li>
				<li>
					<p>
						<i class="memMess3"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess4"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess5"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess6"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess3"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p>
						<i class="memMess4"></i>
						<span>通知公告</span>
					</p>
				</li>
				<li>
					<p class="Memplus">
						<i></i>
					</p>
				</li>
				
			</ul>
		</div>
	</div>

</template>

<script>
export default {
	data() {
		return {
			
			url: require('../assets/home/op_user.png'),

		}
	},
	 methods: {
      
        toTestPge(){
        
			debugger;
			this.$router
					.push({path: 'mftcc-loan-web/apply/mfLoanApplyDetails', query: {applyId:"ac71301ba5a7433bb278c15bfbff54aa",appMainFlowId:"mainFlow_f5b0a60ade29423d966732105129821f"}});
				
			}
	
	 },
	created(){
		//获得登陆操作员是否有保理业务个人中心权限
        let factorHomePowerFlag = this.$hasPerm("factor_home");
        if (factorHomePowerFlag) {
            this.$routerMain.push({path:"/mftcc-factor-web/home/factorHome"});
        }
		// 获得登录操作员是否有信贷业务个人中心权限
        let loanHomePowerFlag = this.$hasPerm("loan_home");
        if(loanHomePowerFlag){
            this.$routerMain.push({path:"/mftcc-loan-web/home/loanHome"});
        }
		// 获得登录操作员是否有租赁业务业务个人中心权限
		let leaseHomePowerFlag = this.$hasPerm("lease_home");
		if(leaseHomePowerFlag){
			this.$routerMain.push({path:"/mftcc-lease-web/home/leaseHome"});
		}
	}
}
</script>

<style>
.memberMain {
	background: #F0F5F8;
	height: 100%;
	width: 100%;
	box-sizing: border-box;
	padding: 20px;
}

.memberMain .mftcc-details-card {
	background: #fff;
	border-radius: 4px;
	padding: 20px;
}

.memImg {
	width: 220px;
	padding-top: 20px;
	float: left;
}

.memImg .el-avatar {
	border-radius: 50%;
	display: block;
	margin: 0 auto;
	box-shadow: 6px 6px 16px rgba(74, 126, 195, 0.5);
	margin-bottom: 40px;
}

.el-row .memImg i {
	display: block;
	width: 28px;
	height: 28px;
	margin: 0 auto;
	cursor: pointer;
	background:  no-repeat center center;
}
.el-row .memImg i.menICon1{
	background-image: url(../assets/home/menICon1.png);
}
.el-row .memImg i.menICon2{
	background-image: url(../assets/home/menICon2.png);
}
.el-row .memImg i.menICon3{
	background-image: url(../assets/home/menICon3.png);
}
.el-row .memImg i.menICon4{
	background-image: url(../assets/home/menICon4.png);
}
.memTit{
	float: left;
	padding-top: 50px;
	margin-left: 10px;
}
.memTit span{
	color: #141414;
	font-size: 20px;
	font-weight: bold;
}
.memTit ul{
	padding: 0;
	margin: 0;
	margin-top: 20px;
}
.memTit li{
	float: left;
	list-style: none;
	margin-right: 27px;
}
.memTit li i{
	width: 16px;
	height: 16px;
	margin-right: 2px;
	display: inline-block;
	position: relative;
	top:2px;
	background: no-repeat center center;
}
.memTit li b{
	color:#000 ;
	opacity: 0.65;
	font-weight: normal;
}
.memTit li i.memTit1{
	background-image: url(../assets/home/memTit1.png);
}
.memTit li i.memTit2{
	background-image: url(../assets/home/memTit2.png);
}
.memTit li i.memTit3{
	background-image: url(../assets/home/memTit3.png);
}
.memData .el-row{
	padding-top: 60px;
}
.memData .el-col-6{
	height: 84px;
	border-right: 1px solid #E9E9E9;
	text-align: center;
	padding-top: 20px;
	box-sizing: border-box;
}
.memData .el-col-6:last-child{
	border: none;
}
.memData .el-col-6 b{
	color: #404040;
	font-weight: normal;
}
.memData .el-col-6 i{
	width: 18px;
	height: 18px;
	margin-right: 2px;
	display: inline-block;
	position: relative;
	top:4px;
	background:  no-repeat center center;
}
.memData .el-col-6 i.memData1{
	background-image: url(../assets/home/memData1.png);
}
.memData .el-col-6 i.memData2{
	background-image: url(../assets/home/memData2.png);
}
.memData .el-col-6 i.memData3{
	background-image: url(../assets/home/memData3.png);
}
.memData .el-col-6 i.memData4{
	background-image: url(../assets/home/memData4.png);
}

.memData .el-col-6 h5{
	color: #141414;
	font-size: 36px;
	padding: 0;
	margin: 0 auto;
	width: 150px;
	text-align: right;
	margin-top: 10px;
	font-weight: 500;
}

.memberMain .mftcc-details-card.messageCenter{
	margin-top: 20px;
	height: calc(100% - 274px);
	box-sizing: border-box;
	padding: 0;
}
.messageCenter h4{
	margin: 0;
	font-size: 16px;
	color: #141414;
	line-height: 50px;
	padding-left: 24px;
	border-bottom: 1px solid #E9E9E9;
}
.messageCenter ul{
	margin-top: 60px;
	padding: 0 30px;
}
.messageCenter li{
	float: left;
	list-style: none;
	padding: 32px;
	position: relative;
}
.messageCenter li p{
	padding: 0 ;
	margin: 0;
	width: 149px;
	height: 149px;
	box-shadow: 4px 4px 30px rgba(0, 0, 0, 0.1);
	background: #fff;
	border-radius: 7px;
	text-align: center;
	padding-top: 34px;
	box-sizing: border-box;
	cursor: pointer;
}
.messageCenter li p:hover{
	background: #fafafa;
}
.messageCenter li p i{
	width: 41px;
	height: 41px;
	background: no-repeat center center;
	display: block;
	margin: 0px auto;
	margin-bottom: 15px;
}
.messageCenter li p i.memMess1{
	background-image: url(../assets/home/memMess1.png);
}
.messageCenter li p i.memMess2{
	background-image: url(../assets/home/memMess2.png);
}
.messageCenter li p i.memMess3{
	background-image: url(../assets/home/memMess3.png);
}
.messageCenter li p i.memMess4{
	background-image: url(../assets/home/memMess4.png);
}
.messageCenter li p i.memMess5{
	background-image: url(../assets/home/memMess5.png);
}
.messageCenter li p i.memMess6{
	background-image: url(../assets/home/memMess6.png);
}
.messageCenter li p span{
	font-size: 14px;
	color: #3D3D3D;
}
.messageCenter li p.Memplus{
	padding-top: 54px;
}
.messageCenter li p.Memplus i{
	background-image: url(../assets/home/memMess7.png);
}
.messageCenter li b{
	display: block;
	position: absolute;
	right:24px;
	top:24px;
	width: 24px;
	height: 24px;
	background: #E72B00;
	border-radius: 50%;
	color: #fff;
	font-weight: normal;
	text-align: center;
	line-height: 24px;
	font-size: 14px;
}
</style>
