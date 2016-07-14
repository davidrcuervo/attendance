<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="content">
	
	<c:if test="${eventConfirmation.errors['confirmation'] == null}">
		<h2 class="text-center">Thank you</h2>
		<c:if test="${eventConfirmation.status == 'yes'}">
			<p class="text-center">${eventConfirmation.person.name}, It is good that you are coming to the ${eventConfirmation.event.name}</p>
		</c:if>
		<c:if test="${eventConfirmation.status == 'no'}">
			<p class="text-center">${eventConfirmation.person.name}, Sorry to hear that you are not coming to the ${eventConfirmation.event.name}</p>
		</c:if>
	</c:if>
	
	<c:if test="${eventConfirmation.errors['confirmation'] != null}">
		<div class="text-danger">
			<h1 class="text-center"><span class="glyphicon glyphicon-alert"></span></h1>
			<c:forEach var="error" items="${eventConfirmation.errors['confirmation']}">
				<p class="text-center">${error}</p>
			</c:forEach>
		</div>
	</c:if>
</c:set>
<jsp:include page="/WEB-INF/view/templates/thankyou.jsp"></jsp:include>