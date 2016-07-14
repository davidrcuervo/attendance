<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="guest_form">
	<form method="post">
		
		<div class="form-group">
			<label for="guest_email">eMail:</label>
			<input type="email" class="form-control" id="guest_email" name="email" placeholder="e-Mail address" value="${guest.email}">
			<c:if test="${guest.errors['email'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${guest.errors['email'] }">
						<small>${error}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<div class="form-group">
			<label for="guest_name">Name:</label>
			<input type="text" class="form-control" id="guest_name" name="name" placeholder="Name of your new guest" value="${guest.name}">
			<c:if test="${guest.errors['name'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${guest.errors['name'] }">
						<small>${error}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<input type="hidden" name="event_id" value="${event.id}">
		<button type="submit" name="submit" value="${submit_value == null ? 'edit' : submit_value}" class="btn btn-default btn-block">${button_guest_form}</button>
		<a href="${cancel_link == null ? pageContext.request.contextPath : cancel_link}" class="btn btn-danger btn-block">Cancel</a>
		<c:if test="${guest.errors['guest'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${guest.errors['guest'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
	</form>
</c:set>