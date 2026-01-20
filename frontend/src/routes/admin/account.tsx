import { createFileRoute, Outlet } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/account')({
  component: AdminAccountLayout,
})

function AdminAccountLayout() {
  return (
    <div>
      <Outlet />
    </div>
  )
}
