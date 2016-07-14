<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="active_link">guests</c:set>

<c:set scope="request" var="content">
	<div class="row">
		<div class="col-xs-6 text-right">Name:</div>
		<div class="col-xs-6">${person.name}</div>
	</div>
	<div class="row">
		<div class="col-xs-6 text-right">e-Mail:</div>
		<div class="col-xs-6">${person.email}</div>
	</div>
	<div class="row">
		<div class="col-xs-offset-6 col-xs-6">
			<a href="${pageContext.request.contextPath}/guest/edit/${person.urlEncodedEmail}" class="btn btn-default btn-sm">Edit</a>
		</div>
	</div>
	
	<div class="table-responsive">
		<table class="table table-striped">
			<caption>Events for this guy</caption>
			<tr class="info">
				<th>Event</th>
				<th>Date</th>
				<th></th>
			</tr>
			<c:forEach var="event" items="${person.eventsPeople}">
				<tr>
					<td>${event.event.name}</td>
					<td>${event.event.date}</td>
					<td><a href="${pageContext.request.contextPath}/event/${event.event.urlEncodedName}" class="btn btn-default">View</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>

</c:set>

<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>
