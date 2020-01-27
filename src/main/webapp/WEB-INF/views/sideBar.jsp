<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<div id="wapMenu" class="flex-grid no-responsive-future"
		style="height: 100%; display: none; margin-top: 4%;">
		<div class="row" style="height: 100%">
			<div class="cell size-x200" id="cell-sidebar"
				style="background-color: #ffffff; height: 100%;">
				<ul class="sidebar2">
				<li><a href="#" ></a></li>
					<li><a href="parameters" id="param"> <span
							class="mif-list icon" style="font-size: 20px;"></span> <span
							class="title">Parameters for Appraisal</span>

					</a></li>

					<li><a href="slabs"> <span
							class="mif-paragraph-justify icon" style="font-size: 20px;"></span>
							<span class="title">Parameter Slabs</span>

					</a></li>
					<li><a href="wapcalc"> <span class="mif-calculator icon"
							style="font-size: 20px;"></span> <span class="title">Wap
								Calculation Example</span>

					</a></li>
					<li class="active"><a href="showincrement"> <span
							class="mif-plus icon" style="font-size: 20px;"></span> <span
							class="title">Know Your Increment</span>

					</a></li>
					<li><a href="upload"> <span class="mif-file-upload icon"
							style="font-size: 20px;"></span> <span class="title">Upload
								(Manager's Option) </span>
						<!--todo get manager designtions from db  -->

					</a></li>
					<li><a href="showprocessWap"> <span
							class="mif-rocket icon" style="font-size: 20px;"></span> <span
							class="title">Process WAP </span>

					</a></li>
					<li><a href="showPsrPerformanceReview"> <span
							class="mif-rocket icon" style="font-size: 20px;"></span> <span
							class="title">Evaluation By Manager</span>

					</a></li>
				</ul>
			</div>

		</div>
	</div>

	<div id="superUsrMenu" class="flex-grid no-responsive-future"
		style="height: 100%; display: none; margin-top: 4%;">
		<div class="row" style="height: 100%">
			<div class="cell size-x200" id="cell-sidebar"
				style="background-color: #ffffff; height: 100%">
				<ul class="sidebar2">
				<li><a href="#" ></a></li>
					<li class="active"><a href="users" id="users"> <span
							class="mif-users icon" style="font-size: 20px;"></span> <span
							class="title">Users</span>
					</a></li>
					<!-- <li >
                        	<a  href="adminUsers" id="adminUsers">
                            	<span class="mif-users icon" style="font-size: 20px;"></span>
                            	<span class="title">Admin Users</span>
                        	</a>
                        </li> -->
					<li><a href="division"> <span class="mif-flow-tree icon"
							style="font-size: 20px;"></span> <span class="title">Division</span>
					</a></li>
					<!-- <li ><a href="lockHistory">
                            <span class="mif-history icon" style="font-size: 20px;"></span>
                            <span class="title">Lock History</span>
                          
                        </a></li>
                        <li ><a href="deleteWapData">
                            <span class="mif-bin icon" style="font-size: 20px;"></span>
                            <span class="title">Delete Data</span>
                        </a></li> -->
					<li><a href="lockmaster"> <span class="mif-lock icon"
							style="font-size: 20px;"></span> <span class="title">Lock
								Master</span>
					</a></li>

					<li><a href="cloneParam"> <span
							class="mif-flow-parallel icon" style="font-size: 20px;"></span> <span
							class="title">Clone Parameters</span>

					</a></li>

				</ul>
			</div>

		</div>
	</div>
</body>
</html>