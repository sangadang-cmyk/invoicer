import { createFileRoute, Outlet } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/invoice')({
  component: AdminInvoiceLayout,
})

function AdminInvoiceLayout() {
  return (
    <div>
      <Outlet />
    </div>
  )
}
