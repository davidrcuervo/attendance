<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="button_guest_form"><span class="glyphicon glyphicon-plus"></span>  Add new guest</c:set>
<c:set scope="request" var="submit_value">addGuest</c:set>
<c:set scope="request" var="cancel_link">${pageContext.request.contextPath}/event/${event.urlEncodedName}</c:set>
<jsp:include page="/WEB-INF/view/web/forms/guest.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Add guest</h3>
	<h4 class="text-center"><i><b>Event:</b> ${event.name}</i></h4>
	<h4 class="text-center"><i><b>Date and time:</b> ${event.date} at ${event.time}</i></h4>
	<c:if test="${event.dateObject.after(app.ahora)}">
		<div>${guest_form}</div>
	</c:if>
	<c:if test="${event.dateObject.before(app.ahora)}">
		<div class="bg-danger text-danger text-center center-block">
			<h1><span class="glyphicon glyphicon-warning-sign"></span></h1>
			<p>This event is in the past, it is not possible to add more guests.</p>
		</div>
	</c:if>
</c:set>

<c:set scope="request" var="title">Add Guest</c:set>
<c:set scope="request" var="active_link">event</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>
