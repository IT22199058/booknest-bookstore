<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h3 class="mb-3">Place Order</h3>
    <p class="mb-1"><strong><c:out value="${book.title}"/></strong> by <c:out value="${book.author}"/></p>
    <p class="text-muted mb-1">${book.details} &middot; Stock: ${book.stock}</p>
    <p class="mb-1">Price: Rs. <fmt:formatNumber value="${book.price}" minFractionDigits="2"/></p>
    <p class="mb-1">Delivery fee: Rs. <fmt:formatNumber value="${book.calculateDeliveryFee()}" minFractionDigits="2"/></p>
    <p class="mb-3">Your discount: <fmt:formatNumber value="${sessionScope.user.discountRate * 100}" maxFractionDigits="0"/>%</p>
    <form method="post" action="${pageContext.request.contextPath}/orders">
      <input type="hidden" name="action" value="create">
      <input type="hidden" name="bookId" value="${book.id}">
      <div class="mb-3"><label class="form-label">Quantity</label>
        <input name="quantity" type="number" min="1" max="${book.stock}" value="1" class="form-control" required></div>
      <button class="btn btn-success">Confirm order</button>
      <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/books">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="footer.jspf" %>
