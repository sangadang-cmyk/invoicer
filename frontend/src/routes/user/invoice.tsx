import { createFileRoute, Outlet } from '@tanstack/react-router'

export const Route = createFileRoute('/user/invoice')({
  component: UserInvoiceLayout,
})

function UserInvoiceLayout() {
  return (
    <div className="p-2">
      <Outlet />
    </div>
  )
}
