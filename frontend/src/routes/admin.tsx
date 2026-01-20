import { createFileRoute, Outlet } from '@tanstack/react-router'

export const Route = createFileRoute('/admin')({
  component: AdminLayout,
})

function AdminLayout() {
  return (
    <div className="p-2">
      <h1>Admin</h1>
      <Outlet />
    </div>
  )
}
