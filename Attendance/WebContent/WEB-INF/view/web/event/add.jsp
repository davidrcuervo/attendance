<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="button_library_form"><span class="glyphicon glyphicon-plus"></span>  Add new Event</c:set>
<c:set scope="request" var="submit_value">add</c:set>
<jsp:include page="/WEB-INF/view/web/forms/event.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Add new Event</h3>
	<div>${event_form}</div>
</c:set>

<c:set scope="request" var="title">Add Event</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>