<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="content" scope="request">
	<h1 class="text-center">Welcome</h1>
	<h3 class="text-center"><small>Welcome to Attendance.</small></h3>
	
	<div id="login_form">
		<form method="post" action="j_security_check" <%-- onsubmit="return checkForEmail()"--%> >
			<div class="form-group">
				<label for="login_email">Username:</label>
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
					</span>
					<input type="text" class="form-control" id="login_email" name="j_username" placeholder="Username">
				</div>
			</div>
			<div class="form-group">
				<label for="login_password"><span class="" aria-hidden="true"></span> Your password:</label>
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
						<span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
					</span>
					<input class="form-control" type="password" id="login_password" name="j_password" placeholder="Password">
				</div>
				
			</div>
				
			<button type="submit" class="btn btn-primary btn-lg btn-block"><span class="glyphicon glyphicon-log-in"></span>  Log In</button>
		</form>
		<div id="ajaxResult_validateMail"></div>
		<c:if test="${loginError != null}">
			<div class="alert alert-danger text-center">
				<strong>Login error: </strong>${loginError}
			</div>
		</c:if>
	</div>
</c:set>

<jsp:include page="/WEB-INF/view/templates/login.jsp"></jsp:include>