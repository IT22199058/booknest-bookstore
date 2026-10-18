<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>

<style>
  /* Reuse the font and scope from the homepage */
  @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap');

  .booknest-ui {
    font-family: 'Plus Jakarta Sans', sans-serif;
    color: #334155;
  }

  /* --- Top Control Bar --- */
  .page-header-container {
    background: #ffffff;
    border-radius: 16px;
    padding: 1.5rem;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
    border: 1px solid #f1f5f9;
  }

  .search-filter-group .form-control,
  .search-filter-group .form-select {
    border-radius: 50px;
    border: 1px solid #e2e8f0;
    padding-left: 1.25rem;
    padding-right: 1.25rem;
    box-shadow: none;
    background-color: #f8fafc;
    transition: all 0.2s;
  }
  .search-filter-group .form-control:focus,
  .search-filter-group .form-select:focus {
    border-color: #4f46e5;
    background-color: #ffffff;
    box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
  }

  .btn-pill {
    border-radius: 50px;
    font-weight: 600;
    padding: 0.5rem 1.5rem;
  }

  /* --- Premium Table Design --- */
  .table-wrapper {
    background: #ffffff;
    border-radius: 16px;
    box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05);
    border: 1px solid #f1f5f9;
    overflow: hidden; /* Ensures table corners respect the border-radius */
  }

  .modern-table { margin-bottom: 0; }

  .modern-table thead th {
    background-color: #f8fafc;
    color: #64748b;
    font-weight: 700;
    text-transform: uppercase;
    font-size: 0.75rem;
    letter-spacing: 0.05em;
    border-bottom: 2px solid #e2e8f0;
    padding: 1rem 1.25rem;
  }

  .modern-table tbody td {
    padding: 1rem 1.25rem;
    vertical-align: middle;
    border-bottom: 1px solid #f1f5f9;
    color: #475569;
  }

  .modern-table tbody tr:hover { background-color: #f8fafc; }

  /* --- Cell Specifics --- */
  .book-title-cell { font-weight: 700; color: #0f172a; }

  /* Truncate long descriptions so they don't break the table layout */
  .details-cell {
    max-width: 200px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 0.875rem;
  }

  .price-cell { font-weight: 800; color: #10b981; }

  .stock-status { font-size: 0.875rem; font-weight: 700; }
  .stock-in { color: #10b981; }
  .stock-out { color: #ef4444; }
</style>

<div class="booknest-ui container py-4">

  <!-- Top Controls Area -->
  <div class="page-header-container mb-4 d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
    <h3 class="mb-0 fw-bolder" style="color: #0f172a;">Catalogue</h3>

    <form class="d-flex flex-wrap flex-md-nowrap gap-2 search-filter-group" method="get" action="${pageContext.request.contextPath}/books">
      <input name="q" class="form-control" placeholder="Search title or author..." value="<c:out value='${q}'/>">
      <select name="category" class="form-select" style="min-width: 150px;">
        <option value="">All categories</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat}" ${cat eq category ? 'selected' : ''}><c:out value="${cat}"/></option>
        </c:forEach>
      </select>
      <button class="btn btn-dark btn-pill px-4" style="background-color: #0f172a; border: none;">Filter</button>
    </form>

    <c:if test="${sessionScope.user.admin}">
      <a class="btn btn-primary btn-pill fw-bold" style="background: #4f46e5; border: none;" href="${pageContext.request.contextPath}/books?action=new">
        + Add Book
      </a>
    </c:if>
  </div>

  <!-- Data Table Area -->
  <div class="table-wrapper table-responsive mb-5">
    <table class="table modern-table align-middle">
      <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Author</th>
          <th>Category</th>
          <th>Format</th>
          <th>Details</th>
          <th class="text-end">Price</th>
          <th>Stock</th>
          <th class="text-end">Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="b" items="${books}">
          <tr>
            <td class="text-muted small">#${b.id}</td>
            <td class="book-title-cell"><c:out value="${b.title}"/></td>
            <td><c:out value="${b.author}"/></td>
            <td><span class="badge bg-light text-secondary border rounded-pill px-3"><c:out value="${b.category}"/></span></td>
            <td>
              <span class="badge rounded-pill ${b.format eq 'EBOOK' ? 'bg-info text-dark' : 'bg-dark text-white'}">
                ${b.format}
              </span>
            </td>
            <td class="details-cell" title="${b.details}">${b.details}</td>
            <td class="text-end price-cell">Rs. <fmt:formatNumber value="${b.price}" minFractionDigits="2"/></td>
            <td>
              <c:choose>
                <c:when test="${b.inStock}">
                  <span class="stock-status stock-in">● ${b.stock} left</span>
                </c:when>
                <c:otherwise>
                  <span class="stock-status stock-out">● Out</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="text-end text-nowrap">
              <c:choose>

                <%-- Admin Actions --%>
                <c:when test="${sessionScope.user.admin}">
                  <a class="btn btn-sm btn-outline-primary rounded-pill px-3" href="${pageContext.request.contextPath}/books?action=edit&id=${b.id}">Edit</a>
                  <form class="d-inline" method="post" action="${pageContext.request.contextPath}/books" onsubmit="return confirm('Delete this book?')">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${b.id}">
                    <button class="btn btn-sm btn-outline-danger rounded-pill px-3 ms-1">Delete</button>
                  </form>
                </c:when>

                <%-- User Buy Actions --%>
                <c:when test="${not empty sessionScope.user and b.inStock}">
                  <a class="btn btn-sm btn-primary rounded-pill px-4" style="background: #4f46e5; border: none;" href="${pageContext.request.contextPath}/orders?action=new&bookId=${b.id}">Buy Now</a>
                </c:when>

                <%-- Guest Actions --%>
                <c:when test="${empty sessionScope.user}">
                  <a class="btn btn-sm btn-outline-secondary rounded-pill px-3" href="${pageContext.request.contextPath}/login">Log in to buy</a>
                </c:when>

              </c:choose>
            </td>
          </tr>
        </c:forEach>

        <%-- Empty State UI --%>
        <c:if test="${empty books}">
          <tr>
            <td colspan="9" class="text-center py-5 text-muted">
              <div class="fs-1 mb-3">&#128218;</div>
              <h5 class="fw-bold text-dark">No books found</h5>
              <p class="mb-0">Try adjusting your search or category filter to find what you're looking for.</p>
            </td>
          </tr>
        </c:if>

      </tbody>
    </table>
  </div>

</div>
<%@ include file="footer.jspf" %>