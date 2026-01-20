import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/invoice/$invoiceId')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/admin/invoice/$invoiceId"!</div>
}
