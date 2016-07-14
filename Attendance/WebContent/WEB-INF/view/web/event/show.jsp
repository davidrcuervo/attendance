<%@page import="com.laetienda.attendance.entities.Event"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set scope="request" var="bootStyle_2Column_A">col-xs-4 col-sm-6 col-md-6 col-lg-6 text-right</c:set>
<c:set scope="request" var="bootStyle_2Column_B">col-xs-8 col-sm-6 col-md-6 col-lg-6 text-left</c:set>

<c:set scope="request" var="content">
	<h2 class="text-center">${event.name}</h2>
	<p class="text-center"><em>${event.description}</em></p>
	<div class="row">
		<div class="col-xs-6"><h4 class="text-right"><strong>Date:</strong></h4></div>
		<div class="col-xs-6"><h4>${event.date} at ${event.time}</h4></div>
	</div>
	<div class="row">
		<div class="col-xs-6"><h4 class="text-right"><strong>Confirmation deadline:</strong></h4></div>
		<div class="col-xs-6"><h4>${event.quantity} ${event.unit} before</h4></div>
	</div>
	
	<div class="table-responsive">
		<table class="table table-striped">
			<tr class="info">
				<th class="text-center">Status</th>
				<th>Name</th>
				<th></th>
			</tr>
			<c:forEach var="guest" items="${event.guests}">	
				<c:if test="${person.urlEncodedEmail != guest.person.urlEncodedEmail}">
					<tr>
						<td class="text-center"><span class="glyphicon ${guest.isAttending == true ? 'glyphicon-ok' : 'glyphicon-remove'}"></span></td>
						<td>${guest.person.name}</td>
						<td><a href="${pageContext.request.contextPath}/event/${event.urlEncodedName}/guest/${guest.person.urlEncodedEmail}" class="btn btn-default">view</a></td>
					</tr>
				</c:if>
				<c:if test="${person.urlEncodedEmail == guest.person.urlEncodedEmail}">
					<tr class="warning">
						<th class="text-center"><span class="glyphicon ${guest.isAttending == true ? 'glyphicon-ok' : 'glyphicon-remove'}"></span></th>
						<th>${guest.person.name}</th>
						<th>
							<a href="${pageContext.request.contextPath}/guest/edit/${guest.person.urlEncodedEmail}?eventUrl=${event.urlEncodedName}" class="btn btn-default">Edit</a>
							<a href="${pageContext.request.contextPath}/event/${event.urlEncodedName}" class="btn btn-default">Close</a>
						</th>
					</tr>
					<tr class="warning">
						<td colspan="3" >
							<div class="container-fluid">
								<div class="row">
									<div class="${bootStyle_2Column_A}">Is ${person.name} attending the event?</div>
									<div class="${bootStyle_2Column_B}">${guest.isAttending == true ? 'yes' : guest.status == 'no' ? 'No' : 'Dont know'}</div>
								</div>
								<div class="row">
									<div class="${bootStyle_2Column_A}">eMail address:</div>
									<div class="${bootStyle_2Column_B}">${person.email }</div>
								</div>
								<div class="row">
									<div class="${bootStyle_2Column_A}">Yes link:</div>
									<div class="${bootStyle_2Column_B}">${app.url}/confirm/event/${event.urlEncodedName}?guest=${person.urlEncodedEmail}&answer=${event.encrypt('yes')}</div>
								</div>
								<div class="row">
									<div class="${bootStyle_2Column_A}">No link:</div>
									<div class="${bootStyle_2Column_B}">${app.url}/confirm/event/${event.urlEncodedName}?guest=${person.urlEncodedEmail}&answer=${event.encrypt('no')}</div>
								</div>
							</div>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</div>
	<a href="${pageContext.request.contextPath}/event/addGuest/${event.urlEncodedName}" class="btn btn-default btn-block"><span class="glyphicon glyphicon-plus"></span>&nbsp &nbsp Add guest</a>
	<a href="${pageContext.request.contextPath}/event/edit/${event.urlEncodedName}" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-edit"></span>&nbsp &nbspEdit Event</a>
	<a href="${pageContext.request.contextPath}/event/delete/${event.urlEncodedName}" class="btn btn-danger btn-block"><span class="glyphicon glyphicon-trash"></span>&nbsp &nbsp Remove Event</a>
	<c:if test="${event.errors['delete'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${event.errors['delete'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
</c:set>
<jsp:include page="/WEB-INF/view/templates/web.jsp"></jsp:include>