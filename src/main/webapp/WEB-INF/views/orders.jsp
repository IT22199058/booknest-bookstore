<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>
<h3 class="mb-3">${sessionScope.user.admin ? 'All Orders' : 'My Orders'}</h3>
<div class="table-responsive"><table class="table table-striped bg-white align-middle">
  <thead class="table-dark"><tr><th>Order</th><th>Date</th><c:if test="${sessionScope.user.admin}"><th>Customer</th></c:if>
    <th>Book</th><th>Qty</th><th class="text-end">Total (Rs.)</th><th>Status</th><th></th></tr></thead>
  <tbody>
  <c:forEach var="o" items="${orders}">
    <tr>
      <td>${o.id}</td><td>${o.date}</td>
      <c:if test="${sessionScope.user.admin}"><td><c:out value="${o.username}"/></td></c:if>
      <td><c:out value="${o.bookTitle}"/></td><td>${o.quantity}</td>
      <td class="text-end"><fmt:formatNumber value="${o.total}" minFractionDigits="2"/></td>
      <td><span class="badge ${o.status eq 'CANCELLED' ? 'bg-danger' : (o.status eq 'DELIVERED' ? 'bg-success' : 'bg-warning text-dark')}">${o.status}</span></td>
      <td class="text-end text-nowrap">
        <c:if test="${o.status eq 'PLACED'}">
          <form class="d-inline" method="post" action="${pageContext.request.contextPath}/orders"
                onsubmit="return confirm('Cancel this order?')">
            <input type="hidden" name="action" value="cancel"><input type="hidden" name="id" value="${o.id}">
            <button class="btn btn-sm btn-outline-warning">Cancel</button></form>
        </c:if>
        <c:if test="${sessionScope.user.admin}">
          <c:if test="${o.status ne 'CANCELLED'}">
            <form class="d-inline" method="post" action="${pageContext.request.contextPath}/orders">
              <input type="hidden" name="action" value="status"><input type="hidden" name="id" value="${o.id}">
              <select name="status" class="form-select form-select-sm d-inline w-auto" onchange="this.form.submit()">
                <c:forEach var="st" items="PLACED,SHIPPED,DELIVERED">
                  <option ${st eq o.status ? 'selected' : ''}>${st}</option>
                </c:forEach>
              </select></form>
          </c:if>
          <form class="d-inline" method="post" action="${pageContext.request.contextPath}/orders"
                onsubmit="return confirm('Delete this order record?')">
            <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="${o.id}">
            <button class="btn btn-sm btn-outline-danger">Delete</button></form>
        </c:if>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty orders}"><tr><td colspan="8" class="text-center text-muted">No orders yet.</td></tr></c:if>
  </tbody>
</table></div>
<%@ include file="footer.jspf" %>
