<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="email_form">
	<form method="post">
		
		<div class="form-group">
			<label for="guest_email">Name:</label>
			<input type="text" class="form-control" id="email_name" name="name" placeholder="Name to identify your email" value="${email.name}">
			<c:if test="${email.errors['name'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${email.errors['name'] }">
						<small>${error}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<div class="form-group">
			<label for="email_contet">Content:</label>
			<textarea class="form-control" id="email_content" name="content" placeholder="Email in html" rows="15">${email.email}</textarea>
			<c:if test="${email.errors['content'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${email.errors['content'] }">
						<small>${error}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<input type="hidden" name="emailID" value="${email.id == null ? '0' : event.id }">
		<button type="submit" name="submit" value="${submit_value == null ? 'edit' : submit_value}" class="btn btn-default btn-block">${button_email_form}</button>
		<a href="${cancel_link == null ? pageContext.request.contextPath : cancel_link}" class="btn btn-danger btn-block">Cancel</a>
	</form>
</c:set>