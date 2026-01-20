import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/admin/account/')({
  component: AdminAccountList,
})

function AdminAccountList() {
  return (
    <div className="p-2">
      <h2>All Accounts</h2>
      <p>View all accounts placeholder</p>
    </div>
  )
}
