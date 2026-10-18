<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');

  /* Scope the new theme to avoid breaking your existing header/footer */
  .booknest-ui {
    font-family: 'Plus Jakarta Sans', sans-serif;
    color: #334155;
  }

  /* --- Premium Hero Section --- */
  .hero-modern {
    background: linear-gradient(135deg, #4f46e5 0%, #2563eb 100%);
    position: relative;
    overflow: hidden;
    box-shadow: 0 20px 40px rgba(37, 99, 235, 0.15);
    border-radius: 24px;
    padding: 4rem 2rem;
  }

  /* Abstract background circles for depth */
  .hero-modern::before, .hero-modern::after {
    content: '';
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
  }
  .hero-modern::before { width: 300px; height: 300px; top: -100px; left: -100px; }
  .hero-modern::after { width: 400px; height: 400px; bottom: -150px; right: -100px; }

  .hero-content { position: relative; z-index: 2; }

  /* --- Unified Search Bar --- */
  .search-wrapper {
    background: rgba(255, 255, 255, 0.95);
    padding: 6px;
    border-radius: 50px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    max-width: 600px;
    margin: 0 auto;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
  }
  .search-wrapper:focus-within {
    transform: translateY(-2px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  }
  .search-wrapper input {
    border: none;
    background: transparent;
    box-shadow: none !important;
    font-weight: 500;
  }

  /* --- Buttons & Pills --- */
  .btn-pill { border-radius: 50px; font-weight: 600; padding: 0.5rem 1.5rem; letter-spacing: 0.3px; }
  .cat-link { transition: all 0.2s; border-color: #e2e8f0; color: #475569; }
  .cat-link:hover { background: #f8fafc; color: #0f172a; border-color: #cbd5e1; transform: translateY(-1px); }

  /* --- Book Card UI --- */
  .book-card {
    border-radius: 16px;
    border: 1px solid #f1f5f9;
    background: #ffffff;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
  }
  .book-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 20px 40px -5px rgba(15, 23, 42, 0.1);
    border-color: #e2e8f0;
  }

  /* Visual mock cover since real images aren't in the DB yet */
  .book-cover-mock {
    height: 220px;
    background: linear-gradient(145deg, #f8fafc 0%, #e2e8f0 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    color: #cbd5e1;
    font-size: 3rem;
    border-bottom: 1px solid #f1f5f9;
  }

  .format-badge {
    position: absolute;
    top: 12px;
    right: 12px;
    font-size: 0.7rem;
    font-weight: 700;
    letter-spacing: 0.5px;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  }

  .book-info { padding: 1.25rem; }
  .book-title {
    font-weight: 700;
    color: #0f172a;
    font-size: 1.1rem;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    margin-bottom: 0.5rem;
  }
  .book-author { color: #64748b; font-weight: 500; }
  .book-price { color: #10b981; font-weight: 800; font-size: 1.25rem; }
</style>

<!-- Wrap everything in the theme class -->
<div class="booknest-ui container py-4">

  <!-- Hero Section -->
  <div class="hero-modern mb-5 text-center text-white">
    <div class="hero-content">
      <h1 class="display-4 fw-bolder mb-3">Welcome to BookNest</h1>
      <p class="fs-5 text-white-50 mb-4">Printed books and e-books, delivered to you. <strong class="text-white">${bookCount} titles</strong> in our catalogue.</p>

      <!-- Unified Search -->
      <form class="d-flex search-wrapper mb-4" method="get" action="${pageContext.request.contextPath}/books">
        <input name="q" class="form-control form-control-lg px-4" placeholder="Search by title or author..." aria-label="Search">
        <button class="btn btn-warning btn-pill px-4" style="background-color: #fbbf24; border:none; color: #78350f;">Search</button>
      </form>

      <c:if test="${empty sessionScope.user}">
        <div class="mt-4 pt-2">
          <a class="btn btn-outline-light btn-pill me-3" href="${pageContext.request.contextPath}/login">Log In</a>
          <a class="btn btn-light btn-pill text-primary" href="${pageContext.request.contextPath}/register">Create Account</a>
        </div>
      </c:if>
      <c:if test="${not empty sessionScope.user}">
        <div class="mt-4 pt-2">
          <span class="badge bg-white text-primary btn-pill fs-6 px-4 py-2 shadow-sm">
            👋 Hello, <c:out value="${sessionScope.user.fullName}"/>
          </span>
        </div>
      </c:if>
    </div>
  </div>

  <!-- Categories -->
  <div class="d-flex align-items-center justify-content-between mb-4">
    <h4 class="fw-bold mb-0 text-dark">Browse by Category</h4>
    <a class="text-decoration-none fw-bold" style="color: #4f46e5;" href="${pageContext.request.contextPath}/books">View All &rarr;</a>
  </div>
  <div class="mb-5 d-flex flex-wrap gap-2">
    <c:forEach var="cat" items="${categories}">
      <a class="btn btn-pill cat-link border" href="${pageContext.request.contextPath}/books?category=${cat}"><c:out value="${cat}"/></a>
    </c:forEach>
  </div>

  <!-- New Arrivals Grid -->
  <h4 class="fw-bold mb-4 text-dark">New Arrivals</h4>
  <div class="row g-4 mb-5">
    <c:forEach var="b" items="${featured}">
      <div class="col-12 col-sm-6 col-md-4 col-lg-3">
        <div class="card h-100 book-card">

          <!-- Mock Book Cover Area -->
          <div class="book-cover-mock">
            <span class="badge rounded-pill format-badge ${b.format eq 'EBOOK' ? 'bg-info text-dark' : 'bg-dark text-white'}">
              ${b.format}
            </span>
            <!-- Fallback icon (looks like a book) -->
            &#128214;
          </div>

          <div class="book-info d-flex flex-column flex-grow-1">
            <h6 class="book-title"><c:out value="${b.title}"/></h6>
            <p class="book-author small mb-3"><c:out value="${b.author}"/></p>

            <div class="mt-auto d-flex align-items-center justify-content-between">
              <span class="book-price">Rs. <fmt:formatNumber value="${b.price}" minFractionDigits="2"/></span>
            </div>

            <div class="mt-3 pt-3 border-top">
              <c:choose>
                <c:when test="${not empty sessionScope.user and b.inStock}">
                  <a class="btn btn-primary w-100 btn-pill" style="background: #4f46e5; border: none;" href="${pageContext.request.contextPath}/orders?action=new&bookId=${b.id}">
                    Add to Cart
                  </a>
                </c:when>
                <c:when test="${empty sessionScope.user}">
                  <a class="btn btn-outline-secondary w-100 btn-pill" href="${pageContext.request.contextPath}/login">Log in to buy</a>
                </c:when>
                <c:otherwise>
                  <button class="btn w-100 btn-pill bg-light text-muted" disabled>Out of stock</button>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

        </div>
      </div>
    </c:forEach>
  </div>

</div>
<%@ include file="footer.jspf" %>