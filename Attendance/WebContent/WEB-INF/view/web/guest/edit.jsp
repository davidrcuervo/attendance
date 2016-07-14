<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="button_guest_form"><span class="glyphicon glyphicon-edit"></span> Save Changes</c:set>
<c:set scope="request" var="submit_value">editGuest</c:set>
<jsp:include page="/WEB-INF/view/web/forms/guest.jsp"></jsp:include>

<c:set scope="request" var="content">
	<h3 class="text-center">Edit guest</h3>
	<div>${guest_form}</div>
</c:set>

<c:set scope="request" var="active_link">guests</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>