<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set scope="request" var="event_form">
	<form method="post">
		<div class="form-group">
			<label for="event_name">Name:</label>
			<input type="text" class="form-control" id="event_name" name="name" placeholder="Name of event" value="${event.name}">
			<c:if test="${event.errors['name'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${event.errors['name'] }">
						<small>${error}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<div class="form-group">
			<label for="event_description">Description:</label>
			<textarea class="form-control" id="event_description" name="description" placeholder="Description of the event">${event.description}</textarea>
			<c:if test="${event.errors['description'] != null}">
				<div class="text-danger text-center">
					<c:forEach var="error" items="${event.errors['description'] }">
						<small>${error}</small><br />
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<div class="form-horizontal">
			<div class="form-group">
				<label for="event_date" class="col-sm-2 control-label">Date:</label>
				<div class="col-sm-4">
					<input type="date" class="form-control" id="event_date" name="event_date" value="${event.date}">
				</div>
			
				<label for="event_time" class="col-sm-1 control-label">Time:</label>
				<div class="col-sm-2">
					<input type="time" class="form-control" id="event_time" name="event_time" value="${event.time}">
				</div>
			</div>
		</div>
		<c:if test="${event.errors['date'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${event.errors['date'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
		
		<div class="form-horizontal">
			<div class="form-group">
				<label for="event_confirm" class="col-sm-2 control-label">Confirmation limit:</label>
				<div class="col-sm-2">
					<input type="number" class="form-control" id="event_confirm" name="quantity_before" min="0" max="180" value="${event.quantity}">
				</div>
				 
				<div class="col-sm-2">
					<select class="form-control" name="unit_before">
						<option value="none" ${event.unit == null ? 'selected' : ''}>Select one option</option>
						<option value="mins" ${event.unit == 'mins' ? 'selected' : ''}>Minutes</option>
						<option value="Hours" ${event.unit == 'Hours' ? 'selected' : ''}>Hours</option>
						<option value="Days" ${event.unit == 'Days' ? 'selected' : ''}>Days</option>
						<option value="Weeks" ${event.unit == 'Weeks' ? 'selected' : ''}>Weeks</option>
						<option value="Months" ${event.unit == 'Months' ? 'selected' : ''}>Months</option>
					</select>
				</div> 
			</div>
		</div>
		<c:if test="${event.errors['confirmationLimitDate'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${event.errors['confirmationLimitDate'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
		
		<div class="form-horizontal">
			<div class="form-group">
				<label for="event_email" class="col-xs-4 col-sm-2 control-label">eMail template:</label>
				<div class="col-xs-8 col-sm-4">
					<select class="form-control" name="email_template">
						<option value="0" ${event.email.url == null ? 'selected' : ''}>Select one email template to send the invitations</option>
						<c:forEach var="emailOption" items="${user.emails}">
							<option value="${emailOption.id}" ${emailOption.url == event.email.url ? 'selected' : ''}>${emailOption.name}</option> 
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<c:if test="${event.errors['email'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${event.errors['email']}">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
		
		<div class="form-group">
			
			<input type="hidden" name="eventID" value="${event.id == null ? '0' : event.id }">
			<button type="submit" name="submit" value="${submit_value == null ? 'edit' : submit_value}" class="btn btn-default btn-block">${button_library_form}</button>
			<a href="${cancel_link == null ? pageContext.request.contextPath : cancel_link }" class="btn btn-danger btn-block">Cancel</a>
		</div>
  		<c:if test="${event.errors['event'] != null}">
			<div class="text-danger text-center">
				<c:forEach var="error" items="${event.errors['event'] }">
					<small>${error}</small><br />
				</c:forEach>
			</div>
		</c:if>
	</form>
</c:set>