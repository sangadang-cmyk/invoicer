import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/dashboard')({
  component: AdminDashboard,
})

function AdminDashboard() {
  return (
    <div className="p-2">
      <h2>Admin Dashboard</h2>
      <p>Admin dashboard placeholder</p>
    </div>
  )
}
