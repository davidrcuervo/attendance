<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="button_library_form"><span class="glyphicon glyphicon-edit"></span>  Save</c:set>
<c:set scope="request" var="submit_value">edit</c:set>
<c:set scope="request" var="cancel_link">${pageContext.request.contextPath}/event/${event.urlEncodedName}</c:set>
<jsp:include page="/WEB-INF/view/web/forms/event.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Edit ${event.name}</h3>
	<div>${event_form}</div>
</c:set>

<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>