
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="email_server_form">
	<form method="post">
		<div class="form-group">
			<label for="server">Server Address:</label>
			<input type="text" class="form-control" id="server" name="server" placeholder="SMTP address of your eMail server" value="${user.emailServer}">
		</div>
		
		<div class="form-group">
			<label for="port">Server port:</label>
			<input type="text" class="form-control" id="port" name="port" placeholder="Port of you eMail server" value="${user.emailPort}">
		</div>
		
		<div class="form-group">
			<label for="server">eMail address:</label>
			<input type="text" class="form-control" id="username" name="username" placeholder="Address of your eMail account" value="${user.emailUser}">
		</div>
		
		<div class="form-group">
			<label for="server">eMail password:</label>
			<input type="password" class="form-control" id="username" name="password" placeholder="Password of your eMail account" value="${user.emailPassword}">
		</div>
		
		<button type="submit" name="submit" value="email_server" class="btn btn-default btn-block"><span class="glyphicon glyphicon-floppy-disk"></span> Save</button>
		<a href="${cancel_link == null ? pageContext.request.contextPath : cancel_link}" class="btn btn-danger btn-block">Cancel</a>
		
		<c:if test="${user.errors['email server'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${user.errors['email server'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
	</form> 
</c:set>
