import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/user/invoice/')({
  component: UserInvoiceIndexPage,
})

function UserInvoiceIndexPage() {
  return (
      <div className="p-2">
        <h2>My Invoices</h2>
        <p>View my invoices placeholder</p>
      </div>
  )
}