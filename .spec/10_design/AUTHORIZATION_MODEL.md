# Authorization Model

Role-based access control (RBAC).

Roles:
- author
- admin

---

# Access Rules

Public:
- Read approved posts only

Author:
- Create post
- Edit own post (if draft or rejected)
- Submit for review

Admin:
- View pending posts
- Approve posts
- Reject posts

---

# Enforcement

Authorization must be enforced server-side.

Frontend role checks are convenience only.

All non-public endpoints must:
- Validate authenticated user
- Validate role
- Validate resource ownership
